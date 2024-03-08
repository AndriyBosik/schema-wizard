package org.schemawizard.core.migration.operation.resolver.oracle;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.RenameColumnOperation;
import org.schemawizard.core.migration.operation.RenameConstraintOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.ORACLE)
public class OracleRenameConstraintOperationResolver implements OperationResolver<RenameConstraintOperation> {
    private final OperationService operationService;

    public OracleRenameConstraintOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(RenameConstraintOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s %s %s %s %s",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        SqlClause.RENAME,
                        SqlClause.CONSTRAINT,
                        operation.getFrom(),
                        SqlClause.TO,
                        operation.getTo()));
    }
}
