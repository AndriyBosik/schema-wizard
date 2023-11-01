package com.example.migration.operation.resolver.oracle;

import com.example.metadata.DatabaseProvider;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.DropTableOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

@Provider(DatabaseProvider.ORACLE)
public class OracleDropTableOperationResolver implements OperationResolver<DropTableOperation> {
    private final OperationService operationService;

    public OracleDropTableOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropTableOperation operation) {
        return new MigrationInfo(
                String.format(
                        "DROP TABLE %s%s",
                        operation.isIfExists() ? "IF EXISTS " : "",
                        operationService.buildTable(operation)));
    }
}
