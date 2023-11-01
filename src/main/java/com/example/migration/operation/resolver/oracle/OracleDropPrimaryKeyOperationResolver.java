package com.example.migration.operation.resolver.oracle;

import com.example.metadata.DatabaseProvider;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.DropPrimaryKeyOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

@Provider(DatabaseProvider.ORACLE)
public class OracleDropPrimaryKeyOperationResolver implements OperationResolver<DropPrimaryKeyOperation> {
    private final OperationService operationService;

    public OracleDropPrimaryKeyOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropPrimaryKeyOperation operation) {
        return new MigrationInfo(
                String.format(
                        "ALTER TABLE %s DROP CONSTRAINT %s",
                        operationService.buildTable(operation),
                        operation.getName()));
    }
}
