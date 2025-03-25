package org.schemawizard.core.migration.builder.column;

import org.schemawizard.core.migration.metadata.PlainColumnType;
import org.schemawizard.core.migration.operation.AddColumnOperation;

public class BoolColumnBuilder extends AbstractColumnBuilder {
    private boolean nullable = true;
    private Boolean defaultValue;
    private boolean ifNotExists = false;

    private BoolColumnBuilder(String schema, String table, String name) {
        super(schema, table, name);
    }

    public static BoolColumnBuilder builder(String schema, String table) {
        return new BoolColumnBuilder(schema, table, null);
    }

    public static BoolColumnBuilder builder(String schema, String table, String name) {
        return new BoolColumnBuilder(schema, table, name);
    }

    @Override
    public BoolColumnBuilder name(String name) {
        this.name = name;
        return this;
    }

    public BoolColumnBuilder nullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public BoolColumnBuilder defaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public BoolColumnBuilder ifNotExists() {
        this.ifNotExists = true;
        return this;
    }

    @Override
    public AddColumnOperation build() {
        return new AddColumnOperation(
                schema,
                table,
                name,
                PlainColumnType.BOOLEAN,
                null,
                null,
                null,
                null,
                nullable,
                defaultValue == null ? null : String.valueOf(defaultValue),
                ifNotExists);
    }
}
