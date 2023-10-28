package com.example.migration.operation;

public class DropTableOperation implements Operation {
    private final String schema;
    private final String table;
    private final boolean ifExists;

    public DropTableOperation(
            String schema,
            String table,
            boolean ifExists
    ) {
        this.schema = schema;
        this.table = table;
        this.ifExists = ifExists;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public boolean isIfExists() {
        return ifExists;
    }
}
