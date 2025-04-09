package io.github.andriybosik.schemawizard.core.migration.builder.column;

import io.github.andriybosik.schemawizard.core.migration.metadata.PlainColumnType;
import io.github.andriybosik.schemawizard.core.migration.operation.AddColumnOperation;

public class TimestampColumnBuilder extends AbstractColumnBuilder {
    private boolean nullable = true;
    private boolean withTimeZone = false;
    private Integer defaultValue;
    private boolean ifNotExists = false;

    private TimestampColumnBuilder(String schema, String table, String name) {
        super(schema, table, name);
    }

    public static TimestampColumnBuilder builder(String schema, String table) {
        return new TimestampColumnBuilder(schema, table, null);
    }

    public static TimestampColumnBuilder builder(String schema, String table, String name) {
        return new TimestampColumnBuilder(schema, table, name);
    }

    @Override
    public TimestampColumnBuilder name(String name) {
        this.name = name;
        return this;
    }

    public TimestampColumnBuilder nullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public TimestampColumnBuilder withTimeZone(boolean withTimeZone) {
        this.withTimeZone = withTimeZone;
        return this;
    }

    public TimestampColumnBuilder defaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public TimestampColumnBuilder ifNotExists() {
        this.ifNotExists = true;
        return this;
    }

    @Override
    public AddColumnOperation build() {
        return new AddColumnOperation(
                schema,
                table,
                name,
                withTimeZone ? PlainColumnType.TIMESTAMP_WITH_TIME_ZONE : PlainColumnType.TIMESTAMP,
                null,
                null,
                null,
                null,
                nullable,
                defaultValue == null ? null : String.valueOf(defaultValue),
                ifNotExists);
    }
}
