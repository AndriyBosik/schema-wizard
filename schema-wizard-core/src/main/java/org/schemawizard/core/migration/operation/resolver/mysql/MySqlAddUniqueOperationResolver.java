package org.schemawizard.core.migration.operation.resolver.mysql;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.AddUniqueOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.MYSQL)
public class MySqlAddUniqueOperationResolver implements OperationResolver<AddUniqueOperation> {
    private final OperationService operationService;

    public MySqlAddUniqueOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(AddUniqueOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s %s %s (%s)",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        SqlClause.ADD_CONSTRAINT,
                        operation.getName(),
                        SqlClause.UNIQUE,
                        String.join(SqlClause.COMMA_SEPARATOR, operationService.mapColumnNames(operation.getColumns()))));
    }
}
