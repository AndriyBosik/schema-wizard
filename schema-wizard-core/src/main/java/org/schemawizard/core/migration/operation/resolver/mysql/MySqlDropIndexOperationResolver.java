package org.schemawizard.core.migration.operation.resolver.mysql;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.DropIndexOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.MYSQL)
public class MySqlDropIndexOperationResolver implements OperationResolver<DropIndexOperation> {
    private final OperationService operationService;

    public MySqlDropIndexOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropIndexOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s %s",
                        SqlClause.DROP_INDEX,
                        operation.getName(),
                        SqlClause.ON,
                        operationService.buildFullName(operation.getTableSchema(), operation.getTableName())));
    }
}
