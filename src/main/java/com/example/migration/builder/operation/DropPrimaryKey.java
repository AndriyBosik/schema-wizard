package com.example.migration.builder.operation;

import com.example.migration.operation.DropPrimaryKeyOperation;
import com.example.migration.operation.Operation;

public class DropPrimaryKey implements OperationBuilder {
    private final String schema;
    private final String table;
    private String name;

    private DropPrimaryKey(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public static DropPrimaryKey builder(String table) {
        return builder(null, table);
    }

    public static DropPrimaryKey builder(String schema, String table) {
        return new DropPrimaryKey(schema, table);
    }

    public DropPrimaryKey name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Operation build() {
        return new DropPrimaryKeyOperation(
                schema,
                table,
                name);
    }
}
