package org.schemawizard.core.migration.operation.resolver.postgresql;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.DropForeignKeyOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlDropForeignKeyOperationResolver implements OperationResolver<DropForeignKeyOperation> {
    private final OperationService operationService;

    public PostgreSqlDropForeignKeyOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropForeignKeyOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s%s %s",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        SqlClause.DROP_CONSTRAINT,
                        operation.isIfExists() ? (" " + SqlClause.IF_EXISTS) : "",
                        operation.getName()));
    }
}
