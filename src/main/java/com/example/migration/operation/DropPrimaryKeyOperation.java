package com.example.migration.operation;

public class DropPrimaryKeyOperation implements Operation {
    private final String schema;
    private final String table;
    private final String name;

    public DropPrimaryKeyOperation(
            String schema,
            String table,
            String name
    ) {
        this.schema = schema;
        this.table = table;
        this.name = name;
    }

    public String getSchema() {
        return this.schema;
    }

    public String getTable() {
        return table;
    }

    public String getName() {
        return name;
    }
}
