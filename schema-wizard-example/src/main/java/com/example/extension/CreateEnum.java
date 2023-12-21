package com.example.extension;

import org.schemawizard.core.migration.operation.Operation;

public class CreateEnum {
    private String schema;
    private String enumName;
    private String[] values;

    private CreateEnum() {
    }

    public static CreateEnum builder() {
        return new CreateEnum();
    }

    public CreateEnum schema(String schema) {
        this.schema = schema;
        return this;
    }

    public CreateEnum enumName(String enumName) {
        this.enumName = enumName;
        return this;
    }

    public CreateEnum values(String... values) {
        this.values = values;
        return this;
    }

    public Operation build() {
        return new CreateEnumOperation(schema, enumName, values);
    }
}
