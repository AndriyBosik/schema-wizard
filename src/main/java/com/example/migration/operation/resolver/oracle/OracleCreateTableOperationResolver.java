package com.example.migration.operation.resolver.oracle;

import com.example.di.annotation.Qualifier;
import com.example.metadata.DatabaseProvider;
import com.example.metadata.SqlClause;
import com.example.migration.annotation.Provider;
import com.example.migration.factory.ColumnTypeFactory;
import com.example.migration.metadata.ColumnTypeFactoryQualifier;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.AddForeignKeyOperation;
import com.example.migration.operation.AddPrimaryKeyOperation;
import com.example.migration.operation.AddUniqueOperation;
import com.example.migration.operation.CreateTableOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

import java.util.Objects;
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
        return new MigrationInfo(
                String.format(
                        "%s %s (%s, %s)",
                        SqlClause.CREATE_TABLE,
                        operationService.buildTable(operation),
                        buildColumnsDefinitions(operation),
                        buildConstraints(operation)));
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
