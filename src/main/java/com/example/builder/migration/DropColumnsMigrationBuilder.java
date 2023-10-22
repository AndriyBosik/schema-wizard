package com.example.builder.migration;

import com.example.builder.operation.DropColumnOperation;
import com.example.builder.operation.DropColumnsOperation;
import com.example.builder.operation.Operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DropColumnsMigrationBuilder implements MigrationBuilder {
    private final String schema;
    private final String table;
    private final List<String> columns;

    private DropColumnsMigrationBuilder(
            String schema,
            String table
    ) {
        this.schema = schema;
        this.table = table;
        this.columns = new ArrayList<>();
    }

    public static DropColumnsMigrationBuilder builder(
            String table
    ) {
        return builder(null, table);
    }

    public static DropColumnsMigrationBuilder builder(
            String schema,
            String table
    ) {
        return new DropColumnsMigrationBuilder(schema, table);
    }

    public DropColumnsMigrationBuilder columns(String... columns) {
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
