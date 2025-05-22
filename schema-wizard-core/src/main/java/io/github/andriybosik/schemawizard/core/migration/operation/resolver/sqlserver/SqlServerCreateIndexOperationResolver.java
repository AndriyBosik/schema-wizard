package io.github.andriybosik.schemawizard.core.migration.operation.resolver.sqlserver;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.CreateIndexOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.SQLSERVER)
public class SqlServerCreateIndexOperationResolver implements OperationResolver<CreateIndexOperation> {
    private final OperationService operationService;

    public SqlServerCreateIndexOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(CreateIndexOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s %s (%s)",
                        SqlClause.CREATE_INDEX,
                        operation.getName(),
                        SqlClause.ON,
                        operationService.buildTable(operation),
                        String.join(", ", operationService.mapColumnNames(operation.getColumns()))));
    }
}
