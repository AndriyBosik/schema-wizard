package org.schemawizard.core.migration.operation;

public class AddPrimaryKeyOperation extends TableBasedOperation {
    private final String name;
    private final String[] columns;

    public AddPrimaryKeyOperation(
            String schema,
            String table,
            String name,
            String[] columns
    ) {
        super(schema, table);
        this.name = name;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public String[] getColumns() {
        return columns;
    }
}
