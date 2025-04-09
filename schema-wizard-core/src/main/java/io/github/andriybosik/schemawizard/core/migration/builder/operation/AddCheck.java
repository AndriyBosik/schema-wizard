package io.github.andriybosik.schemawizard.core.migration.builder.operation;

import io.github.andriybosik.schemawizard.core.migration.operation.AddCheckOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

public class AddCheck implements OperationBuilder {
    private final String schema;
    private final String table;
    private String name;
    private String condition;

    private AddCheck(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public static AddCheck builder(String table) {
        return new AddCheck(null, table);
    }

    public static AddCheck builder(String schema, String table) {
        return new AddCheck(schema, table);
    }

    public AddCheck name(String name) {
        this.name = name;
        return this;
    }

    public AddCheck condition(String condition) {
        this.condition = condition;
        return this;
    }

    @Override
    public Operation build() {
        return new AddCheckOperation(
                schema,
                table,
                name,
                condition);
    }
}
