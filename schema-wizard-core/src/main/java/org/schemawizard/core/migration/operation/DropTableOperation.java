package org.schemawizard.core.migration.operation;

public class DropTableOperation extends TableBasedOperation {
    private final boolean ifExists;

    public DropTableOperation(
            String schema,
            String table,
            boolean ifExists
    ) {
        super(schema, table);
        this.ifExists = ifExists;
    }

    public boolean isIfExists() {
        return ifExists;
    }
}
