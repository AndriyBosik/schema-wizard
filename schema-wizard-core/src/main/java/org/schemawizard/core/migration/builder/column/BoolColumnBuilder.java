package org.schemawizard.core.migration.builder.column;

import org.schemawizard.core.migration.metadata.PlainColumnType;
import org.schemawizard.core.migration.operation.AddColumnOperation;

public class BoolColumnBuilder implements ColumnBuilder {
    private final String schema;
    private final String table;
    private final String name;
    private boolean nullable = true;
    private Boolean defaultValue;

    private BoolColumnBuilder(String schema, String table, String name) {
        this.schema = schema;
        this.table = table;
        this.name = name;
    }

    public static BoolColumnBuilder builder(String schema, String table, String name) {
        return new BoolColumnBuilder(schema, table, name);
    }

    public BoolColumnBuilder nullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public BoolColumnBuilder defaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
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
                defaultValue == null ? null : String.valueOf(defaultValue));
    }
}
