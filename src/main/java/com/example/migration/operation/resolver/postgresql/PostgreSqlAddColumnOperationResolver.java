package com.example.migration.operation.resolver.postgresql;

import com.example.metadata.DatabaseProvider;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.AddColumnOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlAddColumnOperationResolver implements OperationResolver<AddColumnOperation> {
    private final OperationService operationService;

    public PostgreSqlAddColumnOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(AddColumnOperation operation) {
        return new MigrationInfo(
                String.format(
                        "ALTER TABLE %s ADD COLUMN %s",
                        operationService.buildTable(operation),
                        operationService.buildColumnDefinition(operation)));
    }
}
