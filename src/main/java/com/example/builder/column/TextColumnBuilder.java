package com.example.builder.column;

import com.example.builder.operation.AddColumnOperation;

public class TextColumnBuilder implements ColumnBuilder {
    private final String schema;
    private final String table;
    private final String name;
    private boolean nullable = true;
    private Integer minLength;
    private Integer maxLength;
    private String defaultValue;

    private TextColumnBuilder(String schema, String table, String name) {
        this.schema = schema;
        this.table = table;
        this.name = name;
    }

    public static TextColumnBuilder builder(String schema, String table, String name) {
        return new TextColumnBuilder(schema, table, name);
    }

    public TextColumnBuilder nullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public TextColumnBuilder minLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    public TextColumnBuilder maxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public TextColumnBuilder defaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public AddColumnOperation build() {
        return new AddColumnOperation(
                schema,
                table,
                name,
                "text",
                minLength,
                maxLength,
                null,
                null,
                nullable,
                defaultValue);
    }
}
