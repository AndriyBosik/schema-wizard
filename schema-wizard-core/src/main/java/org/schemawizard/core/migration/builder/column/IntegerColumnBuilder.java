package org.schemawizard.core.migration.builder.column;

import org.schemawizard.core.migration.metadata.PlainColumnType;
import org.schemawizard.core.migration.operation.AddColumnOperation;

public class IntegerColumnBuilder implements ColumnBuilder {
    private final String schema;
    private final String table;
    private String name;
    private boolean nullable = true;
    private Integer defaultValue;
    private boolean ifNotExists = false;

    private IntegerColumnBuilder(String schema, String table, String name) {
        this.schema = schema;
        this.table = table;
        this.name = name;
    }

    public static IntegerColumnBuilder builder(String schema, String table) {
        return new IntegerColumnBuilder(schema, table, null);
    }

    public static IntegerColumnBuilder builder(String schema, String table, String name) {
        return new IntegerColumnBuilder(schema, table, name);
    }

    @Override
    public IntegerColumnBuilder name(String name) {
        this.name = name;
        return this;
    }

    public IntegerColumnBuilder nullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public IntegerColumnBuilder defaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public IntegerColumnBuilder ifNotExists() {
        this.ifNotExists = true;
        return this;
    }

    @Override
    public AddColumnOperation build() {
        return new AddColumnOperation(
                schema,
                table,
                name,
                PlainColumnType.INTEGER,
                null,
                null,
                null,
                null,
                nullable,
                defaultValue == null ? null : String.valueOf(defaultValue),
                ifNotExists);
    }
}
