package org.schemawizard.core.migration.operation;

public class DropCheckOperation extends TableBasedOperation {
    private final String name;
    private final boolean ifExists;

    public DropCheckOperation(
            String schema,
            String table,
            String name,
            boolean ifExists
    ) {
        super(schema, table);
        this.name = name;
        this.ifExists = ifExists;
    }

    public String getName() {
        return name;
    }

    public boolean isIfExists() {
        return ifExists;
    }
}
