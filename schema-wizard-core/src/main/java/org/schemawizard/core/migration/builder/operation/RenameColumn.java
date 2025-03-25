package org.schemawizard.core.migration.builder.operation;

import org.schemawizard.core.migration.operation.Operation;
import org.schemawizard.core.migration.operation.RenameColumnOperation;

public class RenameColumn implements OperationBuilder {
    private final String from;
    private final String to;
    private String schema;
    private String table;

    private RenameColumn(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public static RenameColumn builder(String from, String to) {
        return new RenameColumn(from, to);
    }

    public RenameColumn schema(String schema) {
        this.schema = schema;
        return this;
    }

    public RenameColumn table(String table) {
        this.table = table;
        return this;
    }

    @Override
    public Operation build() {
        return new RenameColumnOperation(
                schema,
                table,
                from,
                to);
    }
}
