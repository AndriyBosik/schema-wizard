package org.schemawizard.core.migration.builder.operation;

import org.schemawizard.core.exception.InvalidMigrationDefinitionException;
import org.schemawizard.core.metadata.ErrorMessage;
import org.schemawizard.core.migration.builder.column.ColumnBuilder;
import org.schemawizard.core.migration.factory.ColumnBuilderFactory;
import org.schemawizard.core.migration.operation.AddColumnOperation;
import org.schemawizard.core.migration.operation.AddColumnsOperation;
import org.schemawizard.core.migration.operation.Operation;
import org.schemawizard.core.utils.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AddColumns implements OperationBuilder {
    private final String schema;
    private final String table;
    private final List<AddColumnOperation> columns;

    private AddColumns(
            String schema,
            String table,
            List<AddColumnOperation> columns
    ) {
        this.schema = schema;
        this.table = table;
        this.columns = columns;
    }

    public static AddColumns builder(
            String table,
            Function<ColumnBuilderFactory, List<ColumnBuilder>> columnsFunction
    ) {
        return builder(null, table, columnsFunction);
    }

    public static AddColumns builder(
            String schema,
            String table,
            Function<ColumnBuilderFactory, List<ColumnBuilder>> columnsFunction
    ) {
        ColumnBuilderFactory factory = new ColumnBuilderFactory(schema, table);
        List<AddColumnOperation> operations = columnsFunction.apply(factory).stream()
                .map(ColumnBuilder::build)
                .collect(Collectors.toList());

        boolean emptyColumnExists = operations.stream()
                .anyMatch(operation -> StringUtils.isBlank(operation.getName()));
        if (emptyColumnExists) {
            throw new InvalidMigrationDefinitionException(
                    String.format(
                            ErrorMessage.EMPTY_COLUMN_NAME_FOR_OPERATION_FORMAT,
                            AddColumns.class.getSimpleName()));
        }

        return new AddColumns(schema, table, operations);
    }

    @Override
    public Operation build() {
        return new AddColumnsOperation(schema, table, columns);
    }
}
