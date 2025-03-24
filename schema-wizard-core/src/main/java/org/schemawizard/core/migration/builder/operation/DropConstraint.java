package org.schemawizard.core.migration.builder.operation;

import org.schemawizard.core.migration.operation.DropConstraintOperation;
import org.schemawizard.core.migration.operation.Operation;

public class DropConstraint implements OperationBuilder {
    private final String schema;
    private final String table;
    private String name;
    private boolean ifExists = false;

    private DropConstraint(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public static DropConstraint builder(String table) {
        return builder(null, table);
    }

    public static DropConstraint builder(String schema, String table) {
        return new DropConstraint(schema, table);
    }

    public DropConstraint name(String name) {
        this.name = name;
        return this;
    }

    public DropConstraint ifExists() {
        this.ifExists = true;
        return this;
    }

    @Override
    public Operation build() {
        return new DropConstraintOperation(
                schema,
                table,
                name,
                ifExists);
    }
}
