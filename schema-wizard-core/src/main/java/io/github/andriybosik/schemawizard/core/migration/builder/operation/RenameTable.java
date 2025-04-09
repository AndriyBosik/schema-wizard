package io.github.andriybosik.schemawizard.core.migration.builder.operation;

import io.github.andriybosik.schemawizard.core.migration.operation.Operation;
import io.github.andriybosik.schemawizard.core.migration.operation.RenameTableOperation;

public class RenameTable implements OperationBuilder {
    private final String schema;
    private final String table;
    private String newName;

    private RenameTable(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public static RenameTable builder(String table) {
        return builder(null, table);
    }

    public static RenameTable builder(String schema, String table) {
        return new RenameTable(schema, table);
    }

    public RenameTable newName(String newName) {
        this.newName = newName;
        return this;
    }

    @Override
    public Operation build() {
        return new RenameTableOperation(
                schema,
                table,
                newName);
    }
}
