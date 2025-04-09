package io.github.andriybosik.schemawizard.core.migration.builder.operation;

import io.github.andriybosik.schemawizard.core.migration.operation.DropCheckOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

public class DropCheck implements OperationBuilder {
    private final String schema;
    private final String table;
    private String name;
    private boolean ifExists = false;

    private DropCheck(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public static DropCheck builder(String table) {
        return builder(null, table);
    }

    public static DropCheck builder(String schema, String table) {
        return new DropCheck(schema, table);
    }

    public DropCheck name(String name) {
        this.name = name;
        return this;
    }

    public DropCheck ifExists() {
        this.ifExists = true;
        return this;
    }

    @Override
    public Operation build() {
        return new DropCheckOperation(
                schema,
                table,
                name,
                ifExists);
    }
}
