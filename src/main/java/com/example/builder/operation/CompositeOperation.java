package com.example.builder.operation;

import java.util.List;

public class CompositeOperation implements Operation {
    private final List<Operation> operations;

    public CompositeOperation(List<Operation> operations) {
        this.operations = operations;
    }

    public List<Operation> getOperations() {
        return operations;
    }
}
