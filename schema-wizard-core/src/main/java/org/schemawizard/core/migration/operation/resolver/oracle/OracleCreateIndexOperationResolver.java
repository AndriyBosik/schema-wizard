package org.schemawizard.core.migration.operation.resolver.oracle;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.CreateIndexOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.ORACLE)
public class OracleCreateIndexOperationResolver implements OperationResolver<CreateIndexOperation> {
    private final OperationService operationService;

    public OracleCreateIndexOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(CreateIndexOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s %s (%s)",
                        SqlClause.CREATE_INDEX,
                        operation.getName(),
                        SqlClause.ON,
                        operationService.buildTable(operation),
                        String.join(", ", operationService.mapColumnNames(operation.getColumns()))));
    }
}
