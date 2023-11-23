package com.example.migration.operation.resolver.postgresql;

import com.example.di.annotation.Qualifier;
import com.example.metadata.DatabaseProvider;
import com.example.metadata.SqlClause;
import com.example.migration.annotation.Provider;
import com.example.migration.factory.ColumnTypeFactory;
import com.example.migration.metadata.ColumnTypeFactoryQualifier;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.AddColumnsOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

import java.util.stream.Collectors;

@Provider(DatabaseProvider.POSTGRESQL)
public class PostgreSqlAddColumnsOperationResolver implements OperationResolver<AddColumnsOperation> {
    private final OperationService operationService;
    private final ColumnTypeFactory columnTypeFactory;

    public PostgreSqlAddColumnsOperationResolver(
            OperationService operationService,
            @Qualifier(ColumnTypeFactoryQualifier.POSTGRESQL) ColumnTypeFactory columnTypeFactory
    ) {
        this.operationService = operationService;
        this.columnTypeFactory = columnTypeFactory;
    }

    @Override
    public MigrationInfo resolve(AddColumnsOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        buildColumnsDefinitions(operation)));
    }

    private String buildColumnsDefinitions(AddColumnsOperation operation) {
        return operation.getColumns().stream()
                .map(columnOperation -> operationService.buildColumnDefinition(columnOperation, columnTypeFactory))
                .map(definition -> String.format("%s %s", SqlClause.ADD_COLUMN, definition))
                .collect(Collectors.joining(SqlClause.COLUMNS_SEPARATOR));
    }
}
