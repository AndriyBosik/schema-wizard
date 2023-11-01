package com.example.migration.builder.operation;

import com.example.migration.operation.AddForeignKeyOperation;
import com.example.migration.metadata.ReferentialAction;

public class AddForeignKey implements OperationBuilder {
    private final String schema;
    private final String table;
    private String[] columns;
    private String name;
    private String foreignSchema;
    private String foreignTable;
    private String[] foreignColumns;
    private ReferentialAction onUpdate = ReferentialAction.NO_ACTION;
    private ReferentialAction onDelete = ReferentialAction.NO_ACTION;

    private AddForeignKey(String table) {
        this(null, table);
    }

    private AddForeignKey(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public static AddForeignKey builder(String table) {
        return builder(null, table);
    }

    public static AddForeignKey builder(String schema, String table) {
        return new AddForeignKey(schema, table);
    }

    public AddForeignKey columns(String... columns) {
        this.columns = columns;
        return this;
    }

    public AddForeignKey name(String name) {
        this.name = name;
        return this;
    }

    public AddForeignKey foreignSchema(String foreignSchema) {
        this.foreignSchema = foreignSchema;
        return this;
    }

    public AddForeignKey foreignTable(String foreignTable) {
        this.foreignTable = foreignTable;
        return this;
    }

    public AddForeignKey foreignColumns(String... foreignColumns) {
        this.foreignColumns = foreignColumns;
        return this;
    }

    public AddForeignKey onUpdate(ReferentialAction onUpdate) {
        this.onUpdate = onUpdate;
        return this;
    }

    public AddForeignKey onDelete(ReferentialAction onDelete) {
        this.onDelete = onDelete;
        return this;
    }

    @Override
    public AddForeignKeyOperation build() {
        return new AddForeignKeyOperation(
                schema,
                table,
                columns,
                name,
                foreignSchema,
                foreignTable,
                foreignColumns,
                onUpdate,
                onDelete);
    }
}
