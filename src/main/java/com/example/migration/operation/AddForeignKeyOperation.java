package com.example.migration.operation;

import com.example.migration.metadata.ReferentialAction;

public class AddForeignKeyOperation implements Operation {
    private final String schema;
    private final String table;
    private final String[] columns;
    private final String name;
    private final String foreignSchema;
    private final String foreignTable;
    private final String[] foreignColumns;
    private final ReferentialAction onUpdate;
    private final ReferentialAction onDelete;

    public AddForeignKeyOperation(
            String schema,
            String table,
            String[] columns,
            String name,
            String foreignSchema,
            String foreignTable,
            String[] foreignColumns,
            ReferentialAction onUpdate,
            ReferentialAction onDelete
    ) {
        this.schema = schema;
        this.table = table;
        this.columns = columns;
        this.name = name;
        this.foreignSchema = foreignSchema;
        this.foreignTable = foreignTable;
        this.foreignColumns = foreignColumns;
        this.onUpdate = onUpdate;
        this.onDelete = onDelete;
    }

    public String getSchema() {
        return this.schema;
    }

    public String getTable() {
        return table;
    }

    public String getName() {
        return name;
    }

    public String[] getColumns() {
        return columns;
    }

    public String getForeignSchema() {
        return foreignSchema;
    }

    public String getForeignTable() {
        return foreignTable;
    }

    public String[] getForeignColumns() {
        return foreignColumns;
    }

    public ReferentialAction getOnUpdate() {
        return onUpdate;
    }

    public ReferentialAction getOnDelete() {
        return onDelete;
    }
}
