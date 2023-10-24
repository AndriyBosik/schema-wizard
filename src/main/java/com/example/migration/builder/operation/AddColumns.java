package com.example.migration.builder.operation;

import com.example.migration.builder.column.ColumnBuilder;
import com.example.migration.factory.ColumnBuilderFactory;
import com.example.migration.operation.AddColumnOperation;
import com.example.migration.operation.AddColumnsOperation;
import com.example.migration.operation.Operation;

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
        return new AddColumns(schema, table, operations);
    }

    @Override
    public Operation build() {
        return new AddColumnsOperation(schema, table, columns);
    }
}
