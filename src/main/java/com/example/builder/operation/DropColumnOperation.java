package com.example.builder.operation;

public class DropColumnOperation implements Operation {
    private final String schema;
    private final String table;
    private final String name;

    public DropColumnOperation(
            String schema,
            String table,
            String name
    ) {
        this.schema = schema;
        this.table = table;
        this.name = name;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public String getName() {
        return name;
    }
}
