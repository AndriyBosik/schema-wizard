package com.example.migration.operation;

public class AddPrimaryKeyOperation implements Operation {
    private final String schema;
    private final String table;
    private final String name;
    private final String[] columns;

    public AddPrimaryKeyOperation(
            String schema,
            String table,
            String name,
            String[] columns
    ) {
        this.schema = schema;
        this.table = table;
        this.name = name;
        this.columns = columns;
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

    public String[] getColumns() {
        return columns;
    }
}
