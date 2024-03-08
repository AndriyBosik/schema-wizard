package org.schemawizard.core.migration.builder.operation;

import org.schemawizard.core.migration.operation.DropIndexOperation;
import org.schemawizard.core.migration.operation.Operation;

public class DropIndex implements OperationBuilder {
    private String schema;
    private String name;
    private boolean ifExists = false;

    private DropIndex() {
    }

    public static DropIndex builder() {
        return new DropIndex();
    }

    public DropIndex schema(String schema) {
        this.schema = schema;
        return this;
    }

    public DropIndex name(String name) {
        this.name = name;
        return this;
    }

    public DropIndex ifExists() {
        this.ifExists = true;
        return this;
    }

    @Override
    public Operation build() {
        return new DropIndexOperation(schema, name, ifExists);
    }
}
