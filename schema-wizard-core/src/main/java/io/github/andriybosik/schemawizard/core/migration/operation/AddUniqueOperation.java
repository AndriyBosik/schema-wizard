package io.github.andriybosik.schemawizard.core.migration.operation;

public class AddUniqueOperation extends TableBasedOperation {
    private final String name;
    private final String[] columns;

    public AddUniqueOperation(
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
