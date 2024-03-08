package org.schemawizard.core.migration.operation.resolver.postgresql;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.RenameColumnOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlRenameColumnOperationResolver implements OperationResolver<RenameColumnOperation> {
    private final OperationService operationService;

    public PostgreSqlRenameColumnOperationResolver(OperationService operationService) {
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
                        operation.getFrom(),
                        SqlClause.TO,
                        operation.getTo()));
    }
}
