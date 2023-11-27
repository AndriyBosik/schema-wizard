package org.schemawizard.core.migration.operation.resolver.oracle;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.DropTableOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.ORACLE)
public class OracleDropTableOperationResolver implements OperationResolver<DropTableOperation> {
    private final OperationService operationService;

    public OracleDropTableOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropTableOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s",
                        SqlClause.DROP_TABLE,
                        operationService.buildTable(operation)));
    }
}
