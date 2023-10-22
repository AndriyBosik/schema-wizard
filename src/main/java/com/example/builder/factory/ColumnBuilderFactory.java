package com.example.builder.factory;

import com.example.builder.column.BoolColumnBuilder;
import com.example.builder.column.IntegerColumnBuilder;
import com.example.builder.column.TextColumnBuilder;

public class ColumnBuilderFactory {
    private final String schema;
    private final String table;

    public ColumnBuilderFactory(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public IntegerColumnBuilder integer(String name) {
        return IntegerColumnBuilder.builder(this.schema, this.table, name);
    }

    public TextColumnBuilder text(String name) {
        return TextColumnBuilder.builder(this.schema, this.table, name);
    }

    public BoolColumnBuilder bool(String name) {
        return BoolColumnBuilder.builder(this.schema, this.table, name);
    }
}
