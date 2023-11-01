package com.example.migration.operation;

import java.util.List;

public class AddColumnsOperation extends TableBasedOperation {
    private final List<AddColumnOperation> columns;

    public AddColumnsOperation(
            String schema,
            String table,
            List<AddColumnOperation> columns
    ) {
        super(schema, table);
        this.columns = columns;
    }

    public List<AddColumnOperation> getColumns() {
        return columns;
    }
}
