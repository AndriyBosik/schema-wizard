package io.github.andriybosik.schemawizard.core.migration.operation.resolver.oracle;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.DropForeignKeyOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.ORACLE)
public class OracleDropForeignKeyOperationResolver implements OperationResolver<DropForeignKeyOperation> {
    private final OperationService operationService;

    public OracleDropForeignKeyOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropForeignKeyOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s %s",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        SqlClause.DROP_CONSTRAINT,
                        operation.getName()));
    }
}
