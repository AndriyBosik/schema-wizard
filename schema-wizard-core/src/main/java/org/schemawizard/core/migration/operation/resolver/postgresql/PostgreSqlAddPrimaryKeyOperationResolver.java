package org.schemawizard.core.migration.operation.resolver.postgresql;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.AddPrimaryKeyOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlAddPrimaryKeyOperationResolver implements OperationResolver<AddPrimaryKeyOperation> {
    private final OperationService operationService;

    public PostgreSqlAddPrimaryKeyOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(AddPrimaryKeyOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s %s %s (%s)",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        SqlClause.ADD_CONSTRAINT,
                        operation.getName(),
                        SqlClause.PRIMARY_KEY,
                        String.join(SqlClause.COLUMNS_SEPARATOR, operation.getColumns())));
    }
}
