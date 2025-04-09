package io.github.andriybosik.schemawizard.core.migration.operation.resolver.postgresql;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.CreateIndexOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlCreateIndexOperationResolver implements OperationResolver<CreateIndexOperation> {
    private final OperationService operationService;

    public PostgreSqlCreateIndexOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(CreateIndexOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s%s %s %s%s (%s)",
                        SqlClause.CREATE_INDEX,
                        operation.isIfNotExists() ? (SqlClause.IF_NOT_EXISTS + " ") : "",
                        operation.getName(),
                        SqlClause.ON,
                        operationService.buildTable(operation),
                        operation.getUsing() == null ? "" : (" " + SqlClause.USING + " " + operation.getUsing()),
                        String.join(", ", operationService.mapColumnNames(operation.getColumns()))));
    }
}
