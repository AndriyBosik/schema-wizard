package io.github.andriybosik.schemawizard.core.migration.operation.resolver.oracle;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.AddUniqueOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.ORACLE)
public class OracleAddUniqueOperationResolver implements OperationResolver<AddUniqueOperation> {
    private final OperationService operationService;

    public OracleAddUniqueOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(AddUniqueOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s %s %s (%s)",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        SqlClause.ADD_CONSTRAINT,
                        operation.getName(),
                        SqlClause.UNIQUE,
                        String.join(SqlClause.COMMA_SEPARATOR, operationService.mapColumnNames(operation.getColumns()))));
    }
}
