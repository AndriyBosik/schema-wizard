package io.github.andriybosik.schemawizard.core.migration.operation.resolver.sqlserver;

import io.github.andriybosik.schemawizard.core.di.annotation.Qualifier;
import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.factory.ColumnTypeFactory;
import io.github.andriybosik.schemawizard.core.migration.metadata.ColumnTypeFactoryQualifier;
import io.github.andriybosik.schemawizard.core.migration.metadata.ReferentialAction;
import io.github.andriybosik.schemawizard.core.migration.model.ColumnMetadata;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.AddCheckOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.AddForeignKeyOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.AddPrimaryKeyOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.AddUniqueOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.CreateTableOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;
import io.github.andriybosik.schemawizard.core.utils.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Provider(DatabaseProvider.SQLSERVER)
public class SqlServerCreateTableOperationResolver implements OperationResolver<CreateTableOperation> {
    private final OperationService operationService;
    private final ColumnTypeFactory columnTypeFactory;

    public SqlServerCreateTableOperationResolver(
            OperationService operationService,
            @Qualifier(ColumnTypeFactoryQualifier.SQLSERVER) ColumnTypeFactory columnTypeFactory
    ) {
        this.operationService = operationService;
        this.columnTypeFactory = columnTypeFactory;
    }

    @Override
    public MigrationInfo resolve(CreateTableOperation operation) {
        String constraints = buildConstraints(operation);
        return new MigrationInfo(
                String.format(
                        "%s %s (%s%s)",
                        SqlClause.CREATE_TABLE,
                        operationService.buildTable(operation),
                        buildColumnsDefinitions(operation),
                        StringUtils.isBlank(constraints) ? "" : (", " + constraints)));
    }

    private String buildColumnsDefinitions(CreateTableOperation operation) {
        return operation.getColumns().stream()
                .map(columnOperation -> operationService.buildColumnDefinition(
                        columnOperation,
                        columnTypeFactory,
                        new ColumnMetadata(
                                !operationService.isPrimaryKeyColumn(
                                        columnOperation.getName(),
                                        Optional.ofNullable(operation.getPrimaryKey())
                                                .map(AddPrimaryKeyOperation::getColumns)
                                                .orElse(new String[0])))))
                .collect(Collectors.joining(SqlClause.COMMA_SEPARATOR));
    }

    private String buildConstraints(CreateTableOperation operation) {
        return Stream.of(
                        Stream.of(buildPrimaryKey(operation.getPrimaryKey())),
                        operation.getForeignKeys().stream().map(this::buildForeignKey),
                        operation.getUniques().stream().map(this::buildUnique),
                        operation.getChecks().stream().map(this::buildCheck))
                .flatMap(Function.identity())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(SqlClause.COMMA_SEPARATOR));
    }

    private String buildPrimaryKey(AddPrimaryKeyOperation operation) {
        if (operation == null) {
            return null;
        }
        return String.format(
                "%s%s (%s)",
                operation.getName() == null ? "" : String.format("%s %s ", SqlClause.CONSTRAINT, operation.getName()),
                SqlClause.PRIMARY_KEY,
                String.join(SqlClause.COMMA_SEPARATOR, operationService.mapColumnNames(operation.getColumns())));
    }

    private String buildForeignKey(AddForeignKeyOperation operation) {
        if (operation == null) {
            return null;
        }
        String referentialActionsClause = buildReferentialActions(operation);
        return String.format(
                "%s%s (%s) %s %s (%s)%s",
                operation.getName() == null ? "" : String.format("%s %s ", SqlClause.CONSTRAINT, operation.getName()),
                SqlClause.FOREIGN_KEY,
                String.join(",", operationService.mapColumnNames(operation.getColumns())),
                SqlClause.REFERENCES,
                operationService.buildFullName(operation.getForeignSchema(), operation.getForeignTable()),
                String.join(SqlClause.COMMA_SEPARATOR, operationService.mapColumnNames(operation.getForeignColumns())),
                referentialActionsClause);
    }

    private String buildUnique(AddUniqueOperation operation) {
        if (operation == null) {
            return null;
        }
        return String.format(
                "%s%s (%s)",
                operation.getName() == null ? "" : String.format("%s %s ", SqlClause.CONSTRAINT, operation.getName()),
                SqlClause.UNIQUE,
                String.join(SqlClause.COMMA_SEPARATOR, operationService.mapColumnNames(operation.getColumns())));
    }

    private String buildCheck(AddCheckOperation operation) {
        if (operation == null) {
            return null;
        }
        return String.format(
                "%s%s (%s)",
                operation.getName() == null ? "" : String.format("%s %s ", SqlClause.CONSTRAINT, operation.getName()),
                SqlClause.CHECK,
                operation.getCondition());
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
