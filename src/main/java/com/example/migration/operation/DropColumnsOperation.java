package com.example.migration.operation;

import java.util.List;

public class DropColumnsOperation implements Operation {
    private final String schema;
    private final String table;
    private final List<DropColumnOperation> columns;

    public DropColumnsOperation(
            String schema,
            String table,
            List<DropColumnOperation> columns
    ) {
        this.schema = schema;
        this.table = table;
        this.columns = columns;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public List<DropColumnOperation> getColumns() {
        return columns;
    }
}
