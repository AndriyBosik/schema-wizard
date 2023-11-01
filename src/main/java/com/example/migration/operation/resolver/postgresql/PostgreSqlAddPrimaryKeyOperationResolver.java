package com.example.migration.operation.resolver.postgresql;

import com.example.metadata.DatabaseProvider;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.AddPrimaryKeyOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlAddPrimaryKeyOperationResolver implements OperationResolver<AddPrimaryKeyOperation> {
    private final OperationService operationService;

    public PostgreSqlAddPrimaryKeyOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(AddPrimaryKeyOperation operation) {
        return new MigrationInfo(
                String.format(
                        "ALTER TABLE %s ADD CONSTRAINT %s PRIMARY KEY (%s)",
                        operationService.buildTable(operation),
                        operation.getName(),
                        String.join(",", operation.getColumns())));
    }
}
