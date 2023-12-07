package com.example.custom;

import org.schemawizard.core.migration.operation.TableBasedOperation;

public class AddHashIndexOperation extends TableBasedOperation {
    private final String name;
    private final String[] columns;

    public AddHashIndexOperation(
            String schema,
            String table,
            String name,
            String[] columns
    ) {
        super(schema, table);
        this.name = name;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public String[] getColumns() {
        return columns;
    }
}
