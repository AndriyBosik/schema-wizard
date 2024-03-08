package org.schemawizard.core.migration.builder.column;

import org.schemawizard.core.migration.metadata.PlainColumnType;
import org.schemawizard.core.migration.operation.AddColumnOperation;

public class DecimalColumnBuilder implements ColumnBuilder {
    private final String schema;
    private final String table;
    private String name;
    private boolean nullable = true;
    private Integer precision;
    private Integer scale;
    private Double defaultValue;
    private boolean ifNotExists;

    private DecimalColumnBuilder(String schema, String table, String name) {
        this.schema = schema;
        this.table = table;
        this.name = name;
    }

    public static DecimalColumnBuilder builder(String schema, String table) {
        return builder(schema, table, null);
    }

    public static DecimalColumnBuilder builder(String schema, String table, String name) {
        return new DecimalColumnBuilder(schema, table, name);
    }

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
