package com.example.builder.operation;

import java.util.List;

public class CreateTableOperation implements Operation {
    private final String schema;
    private final String table;
    private final boolean checkIfNotExists;
    private final AddPrimaryKeyOperation primaryKey;
    private final List<AddColumnOperation> columns;
    private final List<AddUniqueConstraintOperation> uniques;

    public CreateTableOperation(
            String schema,
            String table,
            boolean checkIfNotExists,
            AddPrimaryKeyOperation primaryKey,
            List<AddColumnOperation> columns,
            List<AddUniqueConstraintOperation> uniques
    ) {
        this.schema = schema;
        this.table = table;
        this.checkIfNotExists = checkIfNotExists;
        this.primaryKey = primaryKey;
        this.columns = columns;
        this.uniques = uniques;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public boolean isCheckIfNotExists() {
        return checkIfNotExists;
    }

    public AddPrimaryKeyOperation getPrimaryKey() {
        return primaryKey;
    }

    public List<AddColumnOperation> getColumns() {
        return columns;
    }

    public List<AddUniqueConstraintOperation> getUniques() {
        return uniques;
    }
}
