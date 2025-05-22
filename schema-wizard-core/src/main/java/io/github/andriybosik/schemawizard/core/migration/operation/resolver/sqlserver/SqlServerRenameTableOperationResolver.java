package io.github.andriybosik.schemawizard.core.migration.operation.resolver.sqlserver;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.RenameTableOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.SQLSERVER)
public class SqlServerRenameTableOperationResolver implements OperationResolver<RenameTableOperation> {
    private final OperationService operationService;

    public SqlServerRenameTableOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(RenameTableOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s '%s', '%s'",
                        SqlClause.EXEC,
                        SqlClause.SP_RENAME,
                        operationService.buildTable(operation),
                        operation.getNewName()));
    }
}
