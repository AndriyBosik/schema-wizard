package org.schemawizard.core.migration.operation;

public class DropColumnOperation extends TableBasedOperation {
    private final String name;

    public DropColumnOperation(
            String schema,
            String table,
            String name
    ) {
        super(schema, table);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
