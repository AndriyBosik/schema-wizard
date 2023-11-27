package org.schemawizard.core.migration.builder.operation;

import org.schemawizard.core.migration.operation.AddUniqueOperation;
import org.schemawizard.core.migration.operation.Operation;

public class AddUnique implements OperationBuilder {
    private final String schema;
    private final String table;
    private String name;
    private String[] columns;

    private AddUnique(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public static AddUnique builder(String table) {
        return builder(null, table);
    }

    public static AddUnique builder(String schema, String table) {
        return new AddUnique(schema, table);
    }

    public AddUnique name(String name) {
        this.name = name;
        return this;
    }

    public AddUnique columns(String... columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public Operation build() {
        return new AddUniqueOperation(
                schema,
                table,
                name,
                columns);
    }
}
