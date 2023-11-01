package com.example.migration.operation.resolver.postgresql;

import com.example.metadata.DatabaseProvider;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.AddForeignKeyOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlAddForeignKeyOperationResolver implements OperationResolver<AddForeignKeyOperation> {
    private final OperationService operationService;

    public PostgreSqlAddForeignKeyOperationResolver(OperationService operationService) {
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
