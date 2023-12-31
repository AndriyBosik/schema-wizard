package org.schemawizard.core.migration.operation.resolver.postgresql;

import org.schemawizard.core.di.annotation.Qualifier;
import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.factory.ColumnTypeFactory;
import org.schemawizard.core.migration.metadata.ColumnTypeFactoryQualifier;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.AddForeignKeyOperation;
import org.schemawizard.core.migration.operation.AddPrimaryKeyOperation;
import org.schemawizard.core.migration.operation.AddUniqueOperation;
import org.schemawizard.core.migration.operation.CreateTableOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;
import org.schemawizard.core.utils.StringUtils;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlCreateTableOperationResolver implements OperationResolver<CreateTableOperation> {
    private final OperationService operationService;
    private final ColumnTypeFactory columnTypeFactory;

    public PostgreSqlCreateTableOperationResolver(
            OperationService operationService,
            @Qualifier(ColumnTypeFactoryQualifier.POSTGRESQL) ColumnTypeFactory columnTypeFactory
    ) {
        this.operationService = operationService;
        this.columnTypeFactory = columnTypeFactory;
    }

    @Override
    public MigrationInfo resolve(CreateTableOperation operation) {
        String constraints = buildConstraints(operation);
        return new MigrationInfo(
                String.format(
                        "%s %s%s (%s%s)",
                        SqlClause.CREATE_TABLE,
                        operation.isIfNotExists() ? (SqlClause.IF_NOT_EXISTS + " ") : "",
                        operationService.buildTable(operation),
                        buildColumnsDefinitions(operation),
                        StringUtils.isBlank(constraints) ? "" : (", " + constraints)));
    }

    private String buildColumnsDefinitions(CreateTableOperation operation) {
        return operation.getColumns().stream()
                .map(columnOperation -> operationService.buildColumnDefinition(columnOperation, columnTypeFactory))
                .collect(Collectors.joining(SqlClause.COLUMNS_SEPARATOR));
    }

    private String buildConstraints(CreateTableOperation operation) {
        return Stream.of(
                        Stream.of(buildPrimaryKey(operation.getPrimaryKey())),
                        operation.getForeignKeys().stream().map(this::buildForeignKey),
                        operation.getUniques().stream().map(this::buildUnique))
                .flatMap(Function.identity())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(SqlClause.COLUMNS_SEPARATOR));
    }

    private String buildPrimaryKey(AddPrimaryKeyOperation operation) {
        if (operation == null) {
            return null;
        }
        return String.format(
                "%s%s (%s)",
                operation.getName() == null ? "" : String.format("%s %s ", SqlClause.CONSTRAINT, operation.getName()),
                SqlClause.PRIMARY_KEY,
                String.join(SqlClause.COLUMNS_SEPARATOR, operation.getColumns()));
    }

    private String buildForeignKey(AddForeignKeyOperation operation) {
        if (operation == null) {
            return null;
        }
        return String.format(
                "%s%s (%s) %s %s (%s)",
                operation.getName() == null ? "" : String.format("%s %s ", SqlClause.CONSTRAINT, operation.getName()),
                SqlClause.FOREIGN_KEY,
                String.join(",", operation.getColumns()),
                SqlClause.REFERENCES,
                operationService.buildTable(operation.getForeignSchema(), operation.getForeignTable()),
                String.join(SqlClause.COLUMNS_SEPARATOR, operation.getForeignColumns()));
    }

    private String buildUnique(AddUniqueOperation operation) {
        if (operation == null) {
            return null;
        }
        return String.format(
                "%s%s (%s)",
                operation.getName() == null ? "" : String.format("%s %s ", SqlClause.CONSTRAINT, operation.getName()),
                SqlClause.UNIQUE,
                String.join(SqlClause.COLUMNS_SEPARATOR, operation.getColumns()));
    }
}
