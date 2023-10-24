package com.example.migration.builder.operation;

import com.example.migration.operation.CompositeOperation;
import com.example.migration.operation.NativeQueryFileOperation;
import com.example.migration.operation.NativeQueryRawOperation;
import com.example.migration.operation.Operation;
import com.example.exception.InvalidMigrationMetadataException;

import java.util.ArrayList;
import java.util.List;

public class NativeQuery implements OperationBuilder {
    private final List<Operation> operations;

    private NativeQuery() {
        this.operations = new ArrayList<>();
    }

    public static NativeQuery builder() {
        return new NativeQuery();
    }

    public NativeQuery file(String filePath) {
        operations.add(new NativeQueryFileOperation(filePath));
        return this;
    }

    public NativeQuery sql(String sql) {
        operations.add(new NativeQueryRawOperation(sql));
        return this;
    }

    @Override
    public Operation build() {
        if (operations.isEmpty()) {
            throw new InvalidMigrationMetadataException("Native query migration is empty");
        }
        return operations.size() == 1 ? operations.get(0) : new CompositeOperation(operations);
    }
}
