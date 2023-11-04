package com.example.migration.operation.resolver.postgresql;

import com.example.metadata.DatabaseProvider;
import com.example.metadata.SqlClause;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.DropTableOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlDropTableOperationResolver implements OperationResolver<DropTableOperation> {
    private final OperationService operationService;

    public PostgreSqlDropTableOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropTableOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s%s",
                        SqlClause.DROP_TABLE,
                        operation.isIfExists() ? (SqlClause.IF_EXISTS + " ") : "",
                        operationService.buildTable(operation)));
    }
}
