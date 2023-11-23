package com.example.migration.operation.resolver.postgresql;

import com.example.metadata.DatabaseProvider;
import com.example.metadata.SqlClause;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.DropColumnOperation;
import com.example.migration.operation.DropColumnsOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

import java.util.stream.Collectors;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlDropColumnsOperationResolver implements OperationResolver<DropColumnsOperation> {
    private final OperationService operationService;

    public PostgreSqlDropColumnsOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropColumnsOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        buildDropColumnsQuery(operation)));
    }

    private String buildDropColumnsQuery(DropColumnsOperation operation) {
        return operation.getColumns().stream()
                .map(DropColumnOperation::getName)
                .map(name -> String.format("%s %s", SqlClause.DROP_COLUMN, name))
                .collect(Collectors.joining(SqlClause.COLUMNS_SEPARATOR));
    }
}
