package com.example.migration.operation.resolver.oracle;

import com.example.metadata.DatabaseProvider;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.AddUniqueOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

@Provider(DatabaseProvider.ORACLE)
public class OracleAddUniqueOperationResolver implements OperationResolver<AddUniqueOperation> {
    private final OperationService operationService;

    public OracleAddUniqueOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(AddUniqueOperation operation) {
        return new MigrationInfo(
                String.format(
                        "ALTER TABLE %s ADD CONSTRAINT %s UNIQUE (%s)",
                        operationService.buildTable(operation),
                        operation.getName(),
                        String.join(",", operation.getColumns())));
    }
}
