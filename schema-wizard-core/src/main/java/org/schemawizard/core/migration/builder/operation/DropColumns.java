package org.schemawizard.core.migration.builder.operation;

import org.schemawizard.core.migration.operation.DropColumnOperation;
import org.schemawizard.core.migration.operation.DropColumnsOperation;
import org.schemawizard.core.migration.operation.Operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DropColumns implements OperationBuilder {
    private final String schema;
    private final String table;
    private final List<String> columns;
    private final List<String> ifExistsColumns;

    private DropColumns(
            String schema,
            String table
    ) {
        this.schema = schema;
        this.table = table;
        this.columns = new ArrayList<>();
        this.ifExistsColumns = new ArrayList<>();
    }

    public static DropColumns builder(
            String table
    ) {
        return builder(null, table);
    }

    public static DropColumns builder(
            String schema,
            String table
    ) {
        return new DropColumns(schema, table);
    }

    public DropColumns columns(String... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public DropColumns ifExists(String... columns) {
        this.ifExistsColumns.addAll(Arrays.asList(columns));
        return this;
    }

    @Override
    public Operation build() {
        Stream<DropColumnOperation> plainOperations = columns.stream()
                .map(column -> new DropColumnOperation(schema, table, column, false));
        Stream<DropColumnOperation> ifExistsOperations = ifExistsColumns.stream()
                .map(column -> new DropColumnOperation(schema, table, column, true));
        List<DropColumnOperation> operations = Stream.concat(
                        plainOperations,
                        ifExistsOperations)
                .collect(Collectors.toList());
        return new DropColumnsOperation(schema, table, operations);
    }
}
