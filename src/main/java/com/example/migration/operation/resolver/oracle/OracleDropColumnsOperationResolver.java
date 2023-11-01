package com.example.migration.operation.resolver.oracle;

import com.example.metadata.DatabaseProvider;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.DropColumnOperation;
import com.example.migration.operation.DropColumnsOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

import java.util.stream.Collectors;

@Provider(DatabaseProvider.ORACLE)
public class OracleDropColumnsOperationResolver implements OperationResolver<DropColumnsOperation> {
    private final OperationService operationService;

    public OracleDropColumnsOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropColumnsOperation operation) {
        return new MigrationInfo(
                String.format(
                        "ALTER TABLE %s %s",
                        operationService.buildTable(operation),
                        buildDropColumnsQuery(operation)));
    }

    private String buildDropColumnsQuery(DropColumnsOperation operation) {
        return operation.getColumns().stream()
                .map(DropColumnOperation::getName)
                .map(name -> "DROP COLUMN " + name)
                .collect(Collectors.joining(", "));
    }
}
