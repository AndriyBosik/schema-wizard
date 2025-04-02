package org.schemawizard.core.migration.operation.resolver.mysql;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.DropTableOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.MYSQL)
public class MySqlDropTableOperationResolver implements OperationResolver<DropTableOperation> {
    private final OperationService operationService;

    public MySqlDropTableOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropTableOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s%s",
                        SqlClause.DROP_TABLE,
                        operation.isIfExists() ? (SqlClause.IF_EXISTS + " ") : "",
                        operationService.buildTable(operation)));
    }
}
