package com.example.builder.column;

import com.example.builder.operation.AddColumnOperation;

public class GenericColumnBuilder implements ColumnBuilder {
    private final String schema;
    private final String table;
    private final String name;
    private String type;
    private Integer minLength;
    private Integer maxLength;
    private Integer precision;
    private Integer scale;
    private boolean nullable;
    private String sqlDefault;

    private GenericColumnBuilder(String schema, String table, String name) {
        this.schema = schema;
        this.table = table;
        this.name = name;
    }

    public static GenericColumnBuilder builder(String schema, String table, String name) {
        return new GenericColumnBuilder(schema, table, name);
    }

    public GenericColumnBuilder type(String type) {
        this.type = type;
        return this;
    }

    public GenericColumnBuilder minLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    public GenericColumnBuilder maxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public GenericColumnBuilder precision(int precision) {
        this.precision = precision;
        return this;
    }

    public GenericColumnBuilder scale(int scale) {
        this.scale = scale;
        return this;
    }

    public GenericColumnBuilder nullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public GenericColumnBuilder sqlDefault(String sqlDefault) {
        this.sqlDefault = sqlDefault;
        return this;
    }

    @Override
    public AddColumnOperation build() {
        return new AddColumnOperation(
                schema,
                table,
                name,
                type,
                minLength,
                maxLength,
                precision,
                scale,
                nullable,
                sqlDefault);
    }
}
