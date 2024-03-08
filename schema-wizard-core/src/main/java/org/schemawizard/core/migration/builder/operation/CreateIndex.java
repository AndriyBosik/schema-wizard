package org.schemawizard.core.migration.builder.operation;

import org.schemawizard.core.migration.operation.AddUniqueOperation;
import org.schemawizard.core.migration.operation.CreateIndexOperation;
import org.schemawizard.core.migration.operation.Operation;

public class CreateIndex implements OperationBuilder {
    private final String schema;
    private final String table;
    private String name;
    private String using;
    private String[] columns;
    private boolean ifNotExists = false;

    private CreateIndex(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public static CreateIndex builder(String table) {
        return builder(null, table);
    }

    public static CreateIndex builder(String schema, String table) {
        return new CreateIndex(schema, table);
    }

    public CreateIndex name(String name) {
        this.name = name;
        return this;
    }

    public CreateIndex using(String using) {
        this.using = using;
        return this;
    }

    public CreateIndex columns(String... columns) {
        this.columns = columns;
        return this;
    }

    public CreateIndex ifNotExists() {
        this.ifNotExists = true;
        return this;
    }

    @Override
    public Operation build() {
        return new CreateIndexOperation(
                schema,
                table,
                name,
                using,
                columns,
                ifNotExists);
    }
}
