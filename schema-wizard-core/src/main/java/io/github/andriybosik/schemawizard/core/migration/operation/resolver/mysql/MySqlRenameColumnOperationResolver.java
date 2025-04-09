package io.github.andriybosik.schemawizard.core.migration.operation.resolver.mysql;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.RenameColumnOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.MYSQL)
public class MySqlRenameColumnOperationResolver implements OperationResolver<RenameColumnOperation> {
    private final OperationService operationService;

    public MySqlRenameColumnOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(RenameColumnOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s %s %s %s",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        SqlClause.RENAME_COLUMN,
                        operationService.mapColumnName(operation.getFrom()),
                        SqlClause.TO,
                        operationService.mapColumnName(operation.getTo())));
    }
}
