package com.example.builder.column;

import com.example.builder.operation.AddColumnOperation;

public class IntegerColumnBuilder implements ColumnBuilder {
    private final String schema;
    private final String table;
    private final String name;
    private boolean nullable = true;
    private Integer minValue;
    private Integer maxValue;
    private Integer defaultValue;

    private IntegerColumnBuilder(String schema, String table, String name) {
        this.schema = schema;
        this.table = table;
        this.name = name;
    }

    public static IntegerColumnBuilder builder(String schema, String table, String name) {
        return new IntegerColumnBuilder(schema, table, name);
    }

    public IntegerColumnBuilder nullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public IntegerColumnBuilder minValue(int minValue) {
        this.minValue = minValue;
        return this;
    }

    public IntegerColumnBuilder maxValue(int maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    public IntegerColumnBuilder defaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public AddColumnOperation build() {
        return new AddColumnOperation(
                this.schema,
                this.table,
                this.name,
                "integer",
                this.minValue,
                this.maxValue,
                null,
                null,
                this.nullable,
                defaultValue == null ? null : String.valueOf(defaultValue));
    }
}
