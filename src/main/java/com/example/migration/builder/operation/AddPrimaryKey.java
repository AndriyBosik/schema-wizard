package com.example.migration.builder.operation;

import com.example.migration.operation.AddPrimaryKeyOperation;
import com.example.migration.operation.Operation;

public class AddPrimaryKey implements OperationBuilder {
    private final String schema;
    private final String table;
    private String name;
    private String[] columns;

    private AddPrimaryKey(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public static AddPrimaryKey builder(String table) {
        return builder(null, table);
    }

    public static AddPrimaryKey builder(String schema, String table) {
        return new AddPrimaryKey(schema, table);
    }

    public AddPrimaryKey name(String name) {
        this.name = name;
        return this;
    }

    public AddPrimaryKey columns(String... columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public Operation build() {
        return new AddPrimaryKeyOperation(
                schema,
                table,
                name,
                columns);
    }
}
