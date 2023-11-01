package com.example.migration.operation.resolver.oracle;

import com.example.metadata.DatabaseProvider;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.AddPrimaryKeyOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

@Provider(DatabaseProvider.ORACLE)
public class OracleAddPrimaryKeyOperationResolver implements OperationResolver<AddPrimaryKeyOperation> {
    private final OperationService operationService;

    public OracleAddPrimaryKeyOperationResolver(OperationService operationService) {
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
