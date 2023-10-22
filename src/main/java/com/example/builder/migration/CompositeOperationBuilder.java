package com.example.builder.migration;

import com.example.builder.operation.CompositeOperation;
import com.example.builder.operation.Operation;

import java.util.ArrayList;
import java.util.List;

public class CompositeOperationBuilder implements MigrationBuilder {
    private final List<Operation> operations;

    private CompositeOperationBuilder() {
        this.operations = new ArrayList<>();
    }

    public static CompositeOperationBuilder builder() {
        return new CompositeOperationBuilder();
    }

    public CompositeOperationBuilder add(Operation operation) {
        operations.add(operation);
        return this;
    }

    @Override
    public Operation build() {
        return new CompositeOperation(operations);
    }
}
