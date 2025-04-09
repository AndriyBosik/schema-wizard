package io.github.andriybosik.schemawizard.core.migration.operation;

public class RenameColumnOperation extends TableBasedOperation {
    private final String from;
    private final String to;

    public RenameColumnOperation(
            String schema,
            String table,
            String from,
            String to
    ) {
        super(schema, table);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
