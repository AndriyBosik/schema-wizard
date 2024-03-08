package org.schemawizard.core.migration.operation.resolver.oracle;

import org.schemawizard.core.di.annotation.Qualifier;
import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.factory.ColumnTypeFactory;
import org.schemawizard.core.migration.metadata.ColumnTypeFactoryQualifier;
import org.schemawizard.core.migration.metadata.ReferentialAction;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.AddCheckOperation;
import org.schemawizard.core.migration.operation.AddForeignKeyOperation;
import org.schemawizard.core.migration.operation.AddPrimaryKeyOperation;
import org.schemawizard.core.migration.operation.AddUniqueOperation;
import org.schemawizard.core.migration.operation.CreateTableOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;
import org.schemawizard.core.utils.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Provider(DatabaseProvider.ORACLE)
public class OracleCreateTableOperationResolver implements OperationResolver<CreateTableOperation> {

    private final OperationService operationService;
    private final ColumnTypeFactory columnTypeFactory;

    public OracleCreateTableOperationResolver(
            OperationService operationService,
            @Qualifier(ColumnTypeFactoryQualifier.ORACLE) ColumnTypeFactory columnTypeFactory
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
                .map(columnOperation -> operationService.buildColumnDefinition(columnOperation, columnTypeFactory))
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
                operationService.buildTable(operation.getForeignSchema(), operation.getForeignTable()),
                String.join(SqlClause.COMMA_SEPARATOR, operationService.mapColumnNames(operation.getForeignColumns())),
                referentialActionsClause == null ? "" : (" " + referentialActionsClause));
    }

    private String buildUnique(AddUniqueOperation operation) {
        if (operation == null) {
            return null;
        }
        return String.format(
                "%s%s (%s)",
                operation.getName() == null ? "" : String.format("%s %s ", SqlClause.CONSTRAINT, operationService.mapColumnName(operation.getName())),
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
        return Optional.ofNullable(operation.getOnDelete())
                .map(this::mapReferentialAction)
                .map(value -> "ON DELETE " + value)
                .orElse(null);
    }

    private String mapReferentialAction(ReferentialAction action) {
        switch (action) {
            case CASCADE:
                return "CASCADE";
            case SET_NULL:
                return "SET NULL";
        }
        return null;
    }
}
