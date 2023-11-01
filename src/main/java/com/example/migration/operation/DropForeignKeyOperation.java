package com.example.migration.operation;

public class DropForeignKeyOperation extends TableBasedOperation {
    private final String name;

    public DropForeignKeyOperation(
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
