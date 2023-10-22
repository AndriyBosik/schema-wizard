package com.example.builder.operation;

import java.util.List;

public class AddColumnsOperation implements Operation {
    private final String schema;
    private final String table;
    private final List<AddColumnOperation> columns;

    public AddColumnsOperation(
            String schema,
            String table,
            List<AddColumnOperation> columns
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

    public List<AddColumnOperation> getColumns() {
        return columns;
    }
}
