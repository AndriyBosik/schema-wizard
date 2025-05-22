package io.github.andriybosik.schemawizard.core.migration.operation.resolver.sqlserver;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.metadata.ReferentialAction;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.AddForeignKeyOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Provider(DatabaseProvider.SQLSERVER)
public class SqlServerAddForeignKeyOperationResolver implements OperationResolver<AddForeignKeyOperation> {
    private final OperationService operationService;

    public SqlServerAddForeignKeyOperationResolver(OperationService operationService) {
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
        return Stream.of(
                        Optional.ofNullable(operation.getOnDelete())
                                .filter(action -> action.getSupportedProviders().contains(DatabaseProvider.POSTGRESQL))
                                .map(ReferentialAction::getValue)
                                .map(value -> " ON DELETE " + value),
                        Optional.ofNullable(operation.getOnUpdate())
                                .filter(action -> action.getSupportedProviders().contains(DatabaseProvider.POSTGRESQL))
                                .map(ReferentialAction::getValue)
                                .map(value -> " ON UPDATE " + value))
                .map(opt -> opt.orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.joining());
    }
}
