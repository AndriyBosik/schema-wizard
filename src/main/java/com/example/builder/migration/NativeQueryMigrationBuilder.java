package com.example.builder.migration;

import com.example.builder.operation.CompositeOperation;
import com.example.builder.operation.NativeQueryFileOperation;
import com.example.builder.operation.NativeQueryRawOperation;
import com.example.builder.operation.Operation;
import com.example.exception.InvalidMigrationMetadataException;

import java.util.ArrayList;
import java.util.List;

public class NativeQueryMigrationBuilder implements MigrationBuilder {
    private final List<Operation> operations;

    private NativeQueryMigrationBuilder() {
        this.operations = new ArrayList<>();
    }

    public static NativeQueryMigrationBuilder builder() {
        return new NativeQueryMigrationBuilder();
    }

    public NativeQueryMigrationBuilder file(String filePath) {
        operations.add(new NativeQueryFileOperation(filePath));
        return this;
    }

    public NativeQueryMigrationBuilder sql(String sql) {
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
