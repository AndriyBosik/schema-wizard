package com.example.builder.column;

import com.example.builder.operation.AddColumnOperation;

public class IntegerColumnBuilder implements ColumnBuilder {
    private final String schema;
    private final String table;
    private final String name;
    private boolean nullable = true;
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

    public IntegerColumnBuilder defaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public AddColumnOperation build() {
        return new AddColumnOperation(
                schema,
                table,
                name,
                "integer",
                null,
                null,
                null,
                null,
                nullable,
                defaultValue == null ? null : String.valueOf(defaultValue));
    }
}
