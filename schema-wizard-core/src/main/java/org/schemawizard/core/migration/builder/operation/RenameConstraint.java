package org.schemawizard.core.migration.builder.operation;

import org.schemawizard.core.migration.operation.Operation;
import org.schemawizard.core.migration.operation.RenameColumnOperation;
import org.schemawizard.core.migration.operation.RenameConstraintOperation;

public class RenameConstraint implements OperationBuilder {
    private final String from;
    private final String to;
    private String schema;
    private String table;

    private RenameConstraint(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public static RenameConstraint builder(String from, String to) {
        return new RenameConstraint(from, to);
    }

    public RenameConstraint schema(String schema) {
        this.schema = schema;
        return this;
    }

    public RenameConstraint table(String table) {
        this.table = table;
        return this;
    }

    @Override
    public Operation build() {
        return new RenameConstraintOperation(
                schema,
                table,
                from,
                to);
    }
}
