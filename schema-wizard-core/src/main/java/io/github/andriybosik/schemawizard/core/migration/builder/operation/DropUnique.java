package io.github.andriybosik.schemawizard.core.migration.builder.operation;

import io.github.andriybosik.schemawizard.core.migration.operation.DropUniqueOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

public class DropUnique implements OperationBuilder {
    private final String schema;
    private final String table;
    private String name;
    private boolean ifExists = false;

    private DropUnique(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public static DropUnique builder(String table) {
        return builder(null, table);
    }

    public static DropUnique builder(String schema, String table) {
        return new DropUnique(schema, table);
    }

    public DropUnique name(String name) {
        this.name = name;
        return this;
    }

    public DropUnique ifExists() {
        this.ifExists = true;
        return this;
    }

    @Override
    public Operation build() {
        return new DropUniqueOperation(
                schema,
                table,
                name,
                ifExists);
    }
}
