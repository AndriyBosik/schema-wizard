package io.github.andriybosik.schemawizard.core.migration.operation.resolver.sqlserver;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.RenameConstraintOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.SQLSERVER)
public class SqlServerRenameConstraintOperationResolver implements OperationResolver<RenameConstraintOperation> {
    private final OperationService operationService;

    public SqlServerRenameConstraintOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(RenameConstraintOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s '%s', '%s', '%s'",
                        SqlClause.EXEC,
                        SqlClause.SP_RENAME,
                        operationService.buildFullName(operation.getSchema(), operation.getFrom()),
                        operation.getTo(),
                        SqlClause.OBJECT));
    }
}
