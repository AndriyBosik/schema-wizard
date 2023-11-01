package com.example.migration.builder.operation;

import com.example.migration.operation.DropColumnOperation;
import com.example.migration.operation.DropColumnsOperation;
import com.example.migration.operation.Operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DropColumns implements OperationBuilder {
    private final String schema;
    private final String table;
    private final List<String> columns;

    private DropColumns(
            String schema,
            String table
    ) {
        this.schema = schema;
        this.table = table;
        this.columns = new ArrayList<>();
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

    @Override
    public Operation build() {
        List<DropColumnOperation> operations = columns.stream()
                .map(column -> new DropColumnOperation(schema, table, column))
                .collect(Collectors.toList());
        return new DropColumnsOperation(schema, table, operations);
    }
}
