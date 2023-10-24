package com.example.migration.builder.operation;

import com.example.migration.operation.DropUniqueOperation;
import com.example.migration.operation.Operation;

public class DropUnique implements OperationBuilder {
    private final String schema;
    private final String table;
    private String name;

    private DropUnique(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public static DropUnique builder(String table) {
        return builder(null, table);
    }

    public static DropUnique builder(String schema, String table) {
        return new DropUnique(schema, table);
    }

    public DropUnique name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Operation build() {
        return new DropUniqueOperation(
                schema,
                table,
                name);
    }
}
