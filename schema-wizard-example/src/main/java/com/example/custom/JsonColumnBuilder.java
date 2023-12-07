package com.example.custom;

import org.schemawizard.core.migration.builder.column.ColumnBuilder;
import org.schemawizard.core.migration.operation.AddColumnOperation;

public class JsonColumnBuilder implements ColumnBuilder {
    private final String schema;
    private final String table;
    private final String name;

    public JsonColumnBuilder(String schema, String table, String name) {
        this.schema = schema;
        this.table = table;
        this.name = name;
    }

    public JsonColumnBuilder(String name) {
        this(null, null, name);
    }

    @Override
    public AddColumnOperation build() {
        return new AddColumnOperation(
                schema,
                table,
                name,
                "json",
                null,
                null,
                null,
                null,
                false,
                null);
    }
}
