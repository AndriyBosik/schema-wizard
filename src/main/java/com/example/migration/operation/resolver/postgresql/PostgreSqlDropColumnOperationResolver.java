package com.example.migration.operation.resolver.postgresql;

import com.example.metadata.DatabaseProvider;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.DropColumnOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlDropColumnOperationResolver implements OperationResolver<DropColumnOperation> {
    private final OperationService operationService;

    public PostgreSqlDropColumnOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropColumnOperation operation) {
        return new MigrationInfo(
                String.format(
                        "ALTER TABLE %s DROP COLUMN %s",
                        operationService.buildTable(operation),
                        operation.getName()));
    }
}
