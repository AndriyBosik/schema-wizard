package io.github.andriybosik.schemawizard.core.migration.builder.column;

import io.github.andriybosik.schemawizard.core.migration.metadata.PlainColumnType;
import io.github.andriybosik.schemawizard.core.migration.operation.AddColumnOperation;

public class TextColumnBuilder extends AbstractColumnBuilder {
    private boolean nullable = true;
    private Integer minLength;
    private Integer maxLength;
    private String defaultValue;
    private boolean ifNotExists = false;

    private TextColumnBuilder(String schema, String table, String name) {
        super(schema, table, name);
    }

    public static TextColumnBuilder builder(String schema, String table) {
        return new TextColumnBuilder(schema, table, null);
    }

    public static TextColumnBuilder builder(String schema, String table, String name) {
        return new TextColumnBuilder(schema, table, name);
    }

    @Override
    public TextColumnBuilder name(String name) {
        this.name = name;
        return this;
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

    public TextColumnBuilder ifNotExists() {
        this.ifNotExists = true;
        return this;
    }

    @Override
    public AddColumnOperation build() {
        return new AddColumnOperation(
                schema,
                table,
                name,
                PlainColumnType.TEXT,
                minLength,
                maxLength,
                null,
                null,
                nullable,
                defaultValue,
                ifNotExists);
    }
}
