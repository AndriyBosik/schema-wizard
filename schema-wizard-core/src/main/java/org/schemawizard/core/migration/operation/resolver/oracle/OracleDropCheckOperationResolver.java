package org.schemawizard.core.migration.operation.resolver.oracle;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.DropCheckOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.ORACLE)
public class OracleDropCheckOperationResolver implements OperationResolver<DropCheckOperation> {
    private final OperationService operationService;

    public OracleDropCheckOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropCheckOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s %s",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        SqlClause.DROP_CONSTRAINT,
                        operation.getName()));
    }
}
