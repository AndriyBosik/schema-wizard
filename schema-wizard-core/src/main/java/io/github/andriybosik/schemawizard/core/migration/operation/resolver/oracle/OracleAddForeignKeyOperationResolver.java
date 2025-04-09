package io.github.andriybosik.schemawizard.core.migration.operation.resolver.oracle;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.metadata.ReferentialAction;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.AddForeignKeyOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

import java.util.Optional;

@Provider(DatabaseProvider.ORACLE)
public class OracleAddForeignKeyOperationResolver implements OperationResolver<AddForeignKeyOperation> {
    private final OperationService operationService;

    public OracleAddForeignKeyOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(AddForeignKeyOperation operation) {
        String referentialActionsClause = buildReferentialActions(operation);
        return new MigrationInfo(
                String.format(
                        "%s %s %s %s %s (%s) %s %s (%s)%s",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        SqlClause.ADD_CONSTRAINT,
                        operation.getName(),
                        SqlClause.FOREIGN_KEY,
                        String.join(SqlClause.COMMA_SEPARATOR, operationService.mapColumnNames(operation.getColumns())),
                        SqlClause.REFERENCES,
                        operationService.buildFullName(operation.getForeignSchema(), operation.getForeignTable()),
                        String.join(SqlClause.COMMA_SEPARATOR, operationService.mapColumnNames(operation.getForeignColumns())),
                        referentialActionsClause));
    }

    private String buildReferentialActions(AddForeignKeyOperation operation) {
        return Optional.ofNullable(operation.getOnDelete())
                .filter(action -> action.getSupportedProviders().contains(DatabaseProvider.ORACLE))
                .map(ReferentialAction::getValue)
                .map(value -> " ON DELETE " + value)
                .orElse("");
    }
}
