package io.github.andriybosik.schemawizard.core.migration.operation.resolver.sqlserver;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.DropConstraintOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.SQLSERVER)
public class SqlServerDropConstraintOperationResolver implements OperationResolver<DropConstraintOperation> {
    private final OperationService operationService;

    public SqlServerDropConstraintOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropConstraintOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s%s %s",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        SqlClause.DROP_CONSTRAINT,
                        operation.isIfExists() ? (" " + SqlClause.IF_EXISTS) : "",
                        operation.getName()));
    }
}
