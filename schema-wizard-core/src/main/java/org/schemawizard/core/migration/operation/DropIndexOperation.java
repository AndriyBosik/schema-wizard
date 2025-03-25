package org.schemawizard.core.migration.operation;

public class DropIndexOperation implements Operation {
    private final String schema;
    private final String name;
    private final boolean ifExists;

    public DropIndexOperation(String schema, String name, boolean ifExists) {
        this.schema = schema;
        this.name = name;
        this.ifExists = ifExists;
    }

    public String getSchema() {
        return schema;
    }

    public String getName() {
        return name;
    }

    public boolean isIfExists() {
        return ifExists;
    }
}
