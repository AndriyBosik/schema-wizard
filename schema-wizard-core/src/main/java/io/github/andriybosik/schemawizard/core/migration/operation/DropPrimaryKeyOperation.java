package io.github.andriybosik.schemawizard.core.migration.operation;

public class DropPrimaryKeyOperation extends TableBasedOperation {
    private final String name;
    private final boolean ifExists;

    public DropPrimaryKeyOperation(
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
