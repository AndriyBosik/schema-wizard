package org.schemawizard.core.migration.operation;

public class AddCheckOperation extends TableBasedOperation {
    private final String name;
    private final String condition;

    public AddCheckOperation(
            String schema,
            String table,
            String name,
            String condition
    ) {
        super(schema, table);
        this.name = name;
        this.condition = condition;
    }

    public String getName() {
        return name;
    }

    public String getCondition() {
        return condition;
    }
}
