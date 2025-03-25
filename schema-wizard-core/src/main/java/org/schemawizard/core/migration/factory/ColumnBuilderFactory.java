package org.schemawizard.core.migration.factory;

import org.schemawizard.core.migration.builder.column.BoolColumnBuilder;
import org.schemawizard.core.migration.builder.column.DateColumnBuilder;
import org.schemawizard.core.migration.builder.column.DecimalColumnBuilder;
import org.schemawizard.core.migration.builder.column.GenericColumnBuilder;
import org.schemawizard.core.migration.builder.column.IntegerColumnBuilder;
import org.schemawizard.core.migration.builder.column.TextColumnBuilder;
import org.schemawizard.core.migration.builder.column.TimestampColumnBuilder;

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

    public IntegerColumnBuilder newInteger() {
        return IntegerColumnBuilder.builder(schema, table);
    }

    public IntegerColumnBuilder newInteger(String name) {
        return IntegerColumnBuilder.builder(schema, table, name);
    }

    public TextColumnBuilder newText() {
        return TextColumnBuilder.builder(schema, table);
    }

    public TextColumnBuilder newText(String name) {
        return TextColumnBuilder.builder(schema, table, name);
    }

    public BoolColumnBuilder newBool() {
        return BoolColumnBuilder.builder(schema, table);
    }

    public BoolColumnBuilder newBool(String name) {
        return BoolColumnBuilder.builder(schema, table, name);
    }

    public DecimalColumnBuilder newDecimal() {
        return DecimalColumnBuilder.builder(schema, table);
    }

    public DecimalColumnBuilder newDecimal(String name) {
        return DecimalColumnBuilder.builder(schema, table, name);
    }

    public DateColumnBuilder newDate() {
        return DateColumnBuilder.builder(schema, table);
    }

    public DateColumnBuilder newDate(String name) {
        return DateColumnBuilder.builder(schema, table, name);
    }

    public TimestampColumnBuilder newTimestamp() {
        return TimestampColumnBuilder.builder(schema, table);
    }

    public TimestampColumnBuilder newTimestamp(String name) {
        return TimestampColumnBuilder.builder(schema, table, name);
    }
}
