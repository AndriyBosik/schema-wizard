package com.example.builder.operation;

public class DropTableOperation implements Operation {
    private final String schema;
    private final String table;
    private final boolean checkIfExists;

    public DropTableOperation(
            String schema,
            String table,
            boolean checkIfExists
    ) {
        this.schema = schema;
        this.table = table;
        this.checkIfExists = checkIfExists;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public boolean getCheckIfExists() {
        return checkIfExists;
    }
}
