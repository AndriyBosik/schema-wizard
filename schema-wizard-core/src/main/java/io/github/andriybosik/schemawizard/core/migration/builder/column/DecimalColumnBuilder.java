package io.github.andriybosik.schemawizard.core.migration.builder.column;

import io.github.andriybosik.schemawizard.core.migration.metadata.PlainColumnType;
import io.github.andriybosik.schemawizard.core.migration.operation.AddColumnOperation;

public class DecimalColumnBuilder extends AbstractColumnBuilder {
    private boolean nullable = true;
    private Integer precision;
    private Integer scale;
    private Double defaultValue;
    private boolean ifNotExists;

    private DecimalColumnBuilder(String schema, String table, String name) {
        super(schema, table, name);
    }

    public static DecimalColumnBuilder builder(String schema, String table) {
        return builder(schema, table, null);
    }

    public static DecimalColumnBuilder builder(String schema, String table, String name) {
        return new DecimalColumnBuilder(schema, table, name);
    }

    @Override
    public DecimalColumnBuilder name(String name) {
        this.name = name;
        return this;
    }

    public DecimalColumnBuilder nullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public DecimalColumnBuilder precision(int precision) {
        this.precision = precision;
        return this;
    }

    public DecimalColumnBuilder scale(int scale) {
        this.scale = scale;
        return this;
    }

    public DecimalColumnBuilder defaultValue(double defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public DecimalColumnBuilder ifNotExists() {
        this.ifNotExists = true;
        return this;
    }

    @Override
    public AddColumnOperation build() {
        return new AddColumnOperation(
                schema,
                table,
                name,
                PlainColumnType.DECIMAL,
                null,
                null,
                precision,
                scale,
                nullable,
                defaultValue == null ? null : String.valueOf(defaultValue),
                ifNotExists);
    }
}
