package com.example.extension.resolver.operation;

import org.schemawizard.core.migration.operation.Operation;

public class DropEnumOperation implements Operation {
    private final String schema;
    private final String enumName;

    public DropEnumOperation(
            String schema,
            String enumName
    ) {
        this.schema = schema;
        this.enumName = enumName;
    }

    public String getSchema() {
        return schema;
    }

    public String getEnumName() {
        return enumName;
    }
}
