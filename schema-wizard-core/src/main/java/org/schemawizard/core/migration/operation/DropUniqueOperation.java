package org.schemawizard.core.migration.operation;

public class DropUniqueOperation extends TableBasedOperation {
    private final String name;
    private final boolean ifExists;

    public DropUniqueOperation(
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
