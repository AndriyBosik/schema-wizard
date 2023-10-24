package com.example.migration.builder.column;

import com.example.migration.operation.AddColumnOperation;

public class DoubleColumnBuilder implements ColumnBuilder {
    private final String schema;
    private final String table;
    private final String name;
    private boolean nullable = true;
    private Integer precision;
    private Integer scale;
    private Double defaultValue;

    private DoubleColumnBuilder(String schema, String table, String name) {
        this.schema = schema;
        this.table = table;
        this.name = name;
    }

    public static DoubleColumnBuilder builder(String schema, String table, String name) {
        return new DoubleColumnBuilder(schema, table, name);
    }

    public DoubleColumnBuilder nullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public DoubleColumnBuilder precision(int precision) {
        this.precision = precision;
        return this;
    }

    public DoubleColumnBuilder scale(int scale) {
        this.scale = scale;
        return this;
    }

    public DoubleColumnBuilder defaultValue(double defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public AddColumnOperation build() {
        return new AddColumnOperation(
                schema,
                table,
                name,
                "double",
                null,
                null,
                precision,
                scale,
                nullable,
                defaultValue == null ? null : String.valueOf(defaultValue));
    }
}
