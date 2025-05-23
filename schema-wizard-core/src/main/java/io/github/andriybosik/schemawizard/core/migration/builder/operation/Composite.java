package io.github.andriybosik.schemawizard.core.migration.builder.operation;

import io.github.andriybosik.schemawizard.core.migration.operation.CompositeOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

import java.util.ArrayList;
import java.util.List;

public class Composite implements OperationBuilder {
    private final List<Operation> operations;

    private Composite() {
        this.operations = new ArrayList<>();
    }

    public static Composite builder() {
        return new Composite();
    }

    public Composite add(Operation operation) {
        operations.add(operation);
        return this;
    }

    @Override
    public Operation build() {
        return new CompositeOperation(operations);
    }
}
