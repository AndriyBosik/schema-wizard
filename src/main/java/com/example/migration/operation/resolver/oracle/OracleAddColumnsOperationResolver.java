package com.example.migration.operation.resolver.oracle;

import com.example.metadata.DatabaseProvider;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.AddColumnsOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

import java.util.stream.Collectors;

@Provider(DatabaseProvider.ORACLE)
public class OracleAddColumnsOperationResolver implements OperationResolver<AddColumnsOperation> {
    private final OperationService operationService;

    public OracleAddColumnsOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(AddColumnsOperation operation) {
        return new MigrationInfo(
                String.format(
                        "ALTER TABLE %s %s",
                        operationService.buildTable(operation),
                        buildColumnsDefinitions(operation)));
    }

    private String buildColumnsDefinitions(AddColumnsOperation operation) {
        return operation.getColumns().stream()
                .map(operationService::buildColumnDefinition)
                .map(definition -> "ADD COLUMN " + definition)
                .collect(Collectors.joining(", "));
    }
}
