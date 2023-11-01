package com.example.migration.operation.resolver.postgresql;

import com.example.metadata.DatabaseProvider;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.DropUniqueOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlDropUniqueOperationResolver implements OperationResolver<DropUniqueOperation> {
    private final OperationService operationService;

    public PostgreSqlDropUniqueOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropUniqueOperation operation) {
        return new MigrationInfo(
                String.format(
                        "ALTER TABLE %s DROP CONSTRAINT %s",
                        operationService.buildTable(operation),
                        operation.getName()));
    }
}
