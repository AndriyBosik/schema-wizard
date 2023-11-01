package com.example.migration.operation.resolver.oracle;

import com.example.metadata.DatabaseProvider;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.AddForeignKeyOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

@Provider(DatabaseProvider.ORACLE)
public class OracleAddForeignKeyOperationResolver implements OperationResolver<AddForeignKeyOperation> {
    private final OperationService operationService;

    public OracleAddForeignKeyOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(AddForeignKeyOperation operation) {
        return new MigrationInfo(
                String.format(
                        "ALTER TABLE %s ADD CONSTRAINT %s FOREIGN KEY (%s) REFERENCES %s (%s)",
                        operationService.buildTable(operation),
                        operation.getName(),
                        String.join(",", operation.getColumns()),
                        operationService.buildTable(operation.getForeignSchema(), operation.getForeignTable()),
                        String.join(",", operation.getForeignColumns())));
    }
}
