package org.schemawizard.core.migration.operation.resolver.postgresql;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.DropColumnOperation;
import org.schemawizard.core.migration.operation.DropColumnsOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

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
                .map(operationService::mapColumnName)
                .map(name -> String.format("%s %s", SqlClause.DROP_COLUMN, name))
                .collect(Collectors.joining(SqlClause.COLUMNS_SEPARATOR));
    }
}
