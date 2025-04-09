package io.github.andriybosik.schemawizard.core.migration.operation;

import io.github.andriybosik.schemawizard.core.migration.metadata.ReferentialAction;

public class AddForeignKeyOperation extends TableBasedOperation {
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
        super(schema, table);
        this.columns = columns;
        this.name = name;
        this.foreignSchema = foreignSchema;
        this.foreignTable = foreignTable;
        this.foreignColumns = foreignColumns;
        this.onUpdate = onUpdate;
        this.onDelete = onDelete;
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
