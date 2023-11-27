package org.schemawizard.core.migration.builder.operation;

import org.schemawizard.core.migration.operation.DropForeignKeyOperation;

public class DropForeignKey implements OperationBuilder {
    private final String schema;
    private final String table;
    private String name;

    private DropForeignKey(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public static DropForeignKey builder(String table) {
        return builder(null, table);
    }

    public static DropForeignKey builder(String schema, String table) {
        return new DropForeignKey(schema, table);
    }

    public DropForeignKey name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public DropForeignKeyOperation build() {
        return new DropForeignKeyOperation(
                schema,
                table,
                name);
    }
}
