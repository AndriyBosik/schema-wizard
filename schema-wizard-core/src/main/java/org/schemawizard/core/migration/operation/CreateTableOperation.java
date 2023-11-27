package org.schemawizard.core.migration.operation;

import java.util.List;

public class CreateTableOperation extends TableBasedOperation {
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
        super(schema, table);
        this.ifNotExists = ifNotExists;
        this.primaryKey = primaryKey;
        this.columns = columns;
        this.foreignKeys = foreignKeys;
        this.uniques = uniques;
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
