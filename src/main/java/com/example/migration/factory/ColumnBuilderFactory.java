package com.example.migration.factory;

import com.example.migration.builder.column.BoolColumnBuilder;
import com.example.migration.builder.column.GenericColumnBuilder;
import com.example.migration.builder.column.IntegerColumnBuilder;
import com.example.migration.builder.column.TextColumnBuilder;

public class ColumnBuilderFactory {
    private final String schema;
    private final String table;

    public ColumnBuilderFactory(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public GenericColumnBuilder generic(String name) {
        return GenericColumnBuilder.builder(schema, table, name);
    }

    public IntegerColumnBuilder integer(String name) {
        return IntegerColumnBuilder.builder(schema, table, name);
    }

    public TextColumnBuilder text(String name) {
        return TextColumnBuilder.builder(schema, table, name);
    }

    public BoolColumnBuilder bool(String name) {
        return BoolColumnBuilder.builder(schema, table, name);
    }
}