package io.github.andriybosik.schemawizard.core.migration.operation;

public class CreateIndexOperation extends TableBasedOperation {
    private final String name;
    private final String using;
    private final String[] columns;
    private final boolean ifNotExists;

    public CreateIndexOperation(
            String schema,
            String table,
            String name,
            String using,
            String[] columns,
            boolean ifNotExists
    ) {
        super(schema, table);
        this.name = name;
        this.using = using;
        this.columns = columns;
        this.ifNotExists = ifNotExists;
    }

    public String getName() {
        return name;
    }

    public String getUsing() {
        return using;
    }

    public String[] getColumns() {
        return columns;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }
}
