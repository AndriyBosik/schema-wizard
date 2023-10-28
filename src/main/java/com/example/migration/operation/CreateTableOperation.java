package com.example.migration.operation;

import java.util.List;

public class CreateTableOperation implements Operation {
    private final String schema;
    private final String table;
    private final boolean ifNotExists;
    private final AddPrimaryKeyOperation primaryKey;
    private final List<AddColumnOperation> columns;
    private final List<AddForeignKeyOperation> foreignKeys;
    private final List<AddUniqueOperation> uniques;

    public CreateTableOperation(
            String schema,
            String table,
            boolean ifNotExists,
            AddPrimaryKeyOperation primaryKey,
            List<AddColumnOperation> columns,
            List<AddForeignKeyOperation> foreignKeys,
            List<AddUniqueOperation> uniques
    ) {
        this.schema = schema;
        this.table = table;
        this.ifNotExists = ifNotExists;
        this.primaryKey = primaryKey;
        this.columns = columns;
        this.foreignKeys = foreignKeys;
        this.uniques = uniques;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public AddPrimaryKeyOperation getPrimaryKey() {
        return primaryKey;
    }

    public List<AddColumnOperation> getColumns() {
        return columns;
    }

    public List<AddForeignKeyOperation> getForeignKeys() {
        return foreignKeys;
    }

    public List<AddUniqueOperation> getUniques() {
        return uniques;
    }
}
