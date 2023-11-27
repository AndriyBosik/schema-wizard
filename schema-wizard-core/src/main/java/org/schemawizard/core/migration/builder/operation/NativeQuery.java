package org.schemawizard.core.migration.builder.operation;

import org.schemawizard.core.metadata.ErrorMessage;
import org.schemawizard.core.migration.operation.CompositeOperation;
import org.schemawizard.core.migration.operation.NativeQueryFileOperation;
import org.schemawizard.core.migration.operation.NativeQueryRawOperation;
import org.schemawizard.core.migration.operation.Operation;
import org.schemawizard.core.exception.InvalidMigrationDefinitionException;

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
            throw new InvalidMigrationDefinitionException(ErrorMessage.EMPTY_NATIVE_QUERY_MIGRATION);
        }
        return operations.size() == 1 ? operations.get(0) : new CompositeOperation(operations);
    }
}
