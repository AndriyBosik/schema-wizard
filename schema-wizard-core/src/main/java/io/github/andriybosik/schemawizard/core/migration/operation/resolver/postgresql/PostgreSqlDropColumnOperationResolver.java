package io.github.andriybosik.schemawizard.core.migration.operation.resolver.postgresql;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.DropColumnOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlDropColumnOperationResolver implements OperationResolver<DropColumnOperation> {
    private final OperationService operationService;

    public PostgreSqlDropColumnOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropColumnOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s%s %s",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        SqlClause.DROP_COLUMN,
                        operation.isIfExists() ? (" " + SqlClause.IF_EXISTS) : "",
                        operationService.mapColumnName(operation.getName())));
    }
}
