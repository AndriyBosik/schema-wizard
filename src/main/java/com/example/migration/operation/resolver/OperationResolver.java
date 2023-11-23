package com.example.migration.operation.resolver;

import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.Operation;

public interface OperationResolver<T extends Operation> {
    MigrationInfo resolve(T operation);
}
