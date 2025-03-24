package org.schemawizard.core.migration.operation.resolver.oracle;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.metadata.ReferentialAction;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.AddForeignKeyOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

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
                .map(ReferentialAction::getValue)
                .map(value -> " ON DELETE " + value)
                .orElse("");
    }
}
