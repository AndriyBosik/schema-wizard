package org.schemawizard.core.migration.operation.resolver.mysql;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.DropUniqueOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.MYSQL)
public class MySqlDropUniqueOperationResolver implements OperationResolver<DropUniqueOperation> {
    private final OperationService operationService;

    public MySqlDropUniqueOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropUniqueOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s %s",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        SqlClause.DROP_CONSTRAINT,
                        operation.getName()));
    }
}
