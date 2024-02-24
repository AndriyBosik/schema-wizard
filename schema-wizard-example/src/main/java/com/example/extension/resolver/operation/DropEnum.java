package com.example.extension.resolver.operation;

import org.schemawizard.core.migration.operation.Operation;

public class DropEnum {
    private String schema;
    private String enumName;

    private DropEnum() {
    }

    public static DropEnum builder() {
        return new DropEnum();
    }

    public DropEnum schema(String schema) {
        this.schema = schema;
        return this;
    }

    public DropEnum enumName(String enumName) {
        this.enumName = enumName;
        return this;
    }

    public Operation build() {
        return new DropEnumOperation(schema, enumName);
    }
}
