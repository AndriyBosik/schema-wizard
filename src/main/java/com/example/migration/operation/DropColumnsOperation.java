package com.example.migration.operation;

import java.util.List;

public class DropColumnsOperation extends TableBasedOperation {
    private final List<DropColumnOperation> columns;

    public DropColumnsOperation(
            String schema,
            String table,
            List<DropColumnOperation> columns
    ) {
        super(schema, table);
        this.columns = columns;
    }

    public List<DropColumnOperation> getColumns() {
        return columns;
    }
}
