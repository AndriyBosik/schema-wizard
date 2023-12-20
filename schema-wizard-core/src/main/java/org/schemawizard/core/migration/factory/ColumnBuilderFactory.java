package org.schemawizard.core.migration.factory;

import org.schemawizard.core.migration.builder.column.BoolColumnBuilder;
import org.schemawizard.core.migration.builder.column.GenericColumnBuilder;
import org.schemawizard.core.migration.builder.column.IntegerColumnBuilder;
import org.schemawizard.core.migration.builder.column.TextColumnBuilder;

public class ColumnBuilderFactory {
    private final String schema;
    private final String table;

    public ColumnBuilderFactory(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public GenericColumnBuilder generic() {
        return GenericColumnBuilder.builder(schema, table);
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
