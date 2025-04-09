package io.github.andriybosik.schemawizard.core.migration.operation.resolver.oracle;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.DropIndexOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.ORACLE)
public class OracleDropIndexOperationResolver implements OperationResolver<DropIndexOperation> {
    private final OperationService operationService;

    public OracleDropIndexOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropIndexOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s",
                        SqlClause.DROP_INDEX,
                        operationService.buildFullName(operation.getSchema(), operation.getName())));
    }
}
