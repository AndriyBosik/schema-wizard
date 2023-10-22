package com.example.builder.migration;

import com.example.builder.column.ColumnBuilder;
import com.example.builder.factory.ColumnBuilderFactory;
import com.example.builder.operation.AddColumnOperation;
import com.example.builder.operation.AddColumnsOperation;
import com.example.builder.operation.Operation;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AddColumnsMigrationBuilder implements MigrationBuilder {
    private final String schema;
    private final String table;
    private final List<AddColumnOperation> columns;

    private AddColumnsMigrationBuilder(
            String schema,
            String table,
            List<AddColumnOperation> columns
    ) {
        this.schema = schema;
        this.table = table;
        this.columns = columns;
    }

    public static AddColumnsMigrationBuilder builder(
            String table,
            Function<ColumnBuilderFactory, List<ColumnBuilder>> columnsFunction
    ) {
        return builder(null, table, columnsFunction);
    }

    public static AddColumnsMigrationBuilder builder(
            String schema,
            String table,
            Function<ColumnBuilderFactory, List<ColumnBuilder>> columnsFunction
    ) {
        ColumnBuilderFactory factory = new ColumnBuilderFactory(schema, table);
        List<AddColumnOperation> operations = columnsFunction.apply(factory).stream()
                .map(ColumnBuilder::build)
                .collect(Collectors.toList());
        return new AddColumnsMigrationBuilder(schema, table, operations);
    }

    @Override
    public Operation build() {
        return new AddColumnsOperation(schema, table, columns);
    }
}
