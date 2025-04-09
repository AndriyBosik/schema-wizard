package io.github.andriybosik.schemawizard.core.migration.builder.operation;

import io.github.andriybosik.schemawizard.core.migration.operation.DropPrimaryKeyOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

public class DropPrimaryKey implements OperationBuilder {
    private final String schema;
    private final String table;
    private String name;
    private boolean ifExists = false;

    private DropPrimaryKey(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public static DropPrimaryKey builder(String table) {
        return builder(null, table);
    }

    public static DropPrimaryKey builder(String schema, String table) {
        return new DropPrimaryKey(schema, table);
    }

    public DropPrimaryKey name(String name) {
        this.name = name;
        return this;
    }

    public DropPrimaryKey ifExists() {
        this.ifExists = true;
        return this;
    }

    @Override
    public Operation build() {
        return new DropPrimaryKeyOperation(
                schema,
                table,
                name,
                ifExists);
    }
}
