package com.example.migration.operation;

public class DropPrimaryKeyOperation extends TableBasedOperation {
    private final String name;

    public DropPrimaryKeyOperation(
            String schema,
            String table,
            String name
    ) {
        super(schema, table);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
