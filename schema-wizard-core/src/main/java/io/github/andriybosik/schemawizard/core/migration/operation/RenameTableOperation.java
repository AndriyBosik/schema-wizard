package io.github.andriybosik.schemawizard.core.migration.operation;

public class RenameTableOperation extends TableBasedOperation {
    private final String newName;

    public RenameTableOperation(
            String schema,
            String table,
            String newName
    ) {
        super(schema, table);
        this.newName = newName;
    }

    public String getNewName() {
        return newName;
    }
}
