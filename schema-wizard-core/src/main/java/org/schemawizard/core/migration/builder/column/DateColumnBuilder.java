package org.schemawizard.core.migration.builder.column;

import org.schemawizard.core.migration.metadata.PlainColumnType;
import org.schemawizard.core.migration.operation.AddColumnOperation;

public class DateColumnBuilder implements ColumnBuilder {
    private final String schema;
    private final String table;
    private String name;
    private boolean nullable = true;
    private Integer defaultValue;
    private boolean ifNotExists = false;

    private DateColumnBuilder(String schema, String table, String name) {
        this.schema = schema;
        this.table = table;
        this.name = name;
    }

    public static DateColumnBuilder builder(String schema, String table) {
        return new DateColumnBuilder(schema, table, null);
    }

    public static DateColumnBuilder builder(String schema, String table, String name) {
        return new DateColumnBuilder(schema, table, name);
    }

    @Override
    public DateColumnBuilder name(String name) {
        this.name = name;
        return this;
    }

    public DateColumnBuilder nullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public DateColumnBuilder defaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public DateColumnBuilder ifNotExists() {
        this.ifNotExists = true;
        return this;
    }

    @Override
    public AddColumnOperation build() {
        return new AddColumnOperation(
                schema,
                table,
                name,
                PlainColumnType.DATE,
                null,
                null,
                null,
                null,
                nullable,
                defaultValue == null ? null : String.valueOf(defaultValue),
                ifNotExists);
    }
}
