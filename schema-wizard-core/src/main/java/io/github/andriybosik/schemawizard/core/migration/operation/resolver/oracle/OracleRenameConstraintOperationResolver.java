package io.github.andriybosik.schemawizard.core.migration.operation.resolver.oracle;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.RenameConstraintOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

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
