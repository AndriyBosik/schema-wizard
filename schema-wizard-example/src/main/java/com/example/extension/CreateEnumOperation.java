package com.example.extension;

import org.schemawizard.core.migration.operation.Operation;

public class CreateEnumOperation implements Operation {
    private final String schema;
    private final String enumName;
    private final String[] values;

    public CreateEnumOperation(
            String schema,
            String enumName,
            String... values
    ) {
        this.schema = schema;
        this.enumName = enumName;
        this.values = values;
    }

    public String getSchema() {
        return schema;
    }

    public String getEnumName() {
        return enumName;
    }

    public String[] getValues() {
        return values;
    }
}
