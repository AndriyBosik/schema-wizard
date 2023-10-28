package com.example.migration.operation;

public class DropUniqueOperation extends TableBasedOperation {
    private final String name;

    public DropUniqueOperation(
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
