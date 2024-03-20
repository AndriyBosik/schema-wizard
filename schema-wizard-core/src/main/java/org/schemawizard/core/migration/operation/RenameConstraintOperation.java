package org.schemawizard.core.migration.operation;

public class RenameConstraintOperation extends TableBasedOperation {
    private final String from;
    private final String to;

    public RenameConstraintOperation(
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
