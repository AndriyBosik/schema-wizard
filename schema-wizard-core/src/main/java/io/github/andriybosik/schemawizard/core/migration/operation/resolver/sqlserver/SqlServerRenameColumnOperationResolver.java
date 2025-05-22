package io.github.andriybosik.schemawizard.core.migration.operation.resolver.sqlserver;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.RenameColumnOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.SQLSERVER)
public class SqlServerRenameColumnOperationResolver implements OperationResolver<RenameColumnOperation> {
    private final OperationService operationService;

    public SqlServerRenameColumnOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(RenameColumnOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s '%s.%s', '%s', '%s'",
                        SqlClause.EXEC,
                        SqlClause.SP_RENAME,
                        operationService.buildTable(operation),
                        operationService.mapColumnName(operation.getFrom()),
                        operationService.mapColumnName(operation.getTo()),
                        SqlClause.COLUMN));
    }
}
