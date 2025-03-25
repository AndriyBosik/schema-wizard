package org.schemawizard.core.migration.operation.resolver.postgresql;

import org.schemawizard.core.di.annotation.Qualifier;
import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.factory.ColumnTypeFactory;
import org.schemawizard.core.migration.metadata.ColumnTypeFactoryQualifier;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.AddColumnsOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

import java.util.AbstractMap;
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
                .map(columnOperation -> new AbstractMap.SimpleEntry<>(
                        columnOperation.isIfNotExists(),
                        operationService.buildColumnDefinition(columnOperation, columnTypeFactory)))
                .map(entry -> String.format(
                        "%s%s %s",
                        SqlClause.ADD_COLUMN,
                        entry.getKey() ? (" " + SqlClause.IF_NOT_EXISTS) : "",
                        entry.getValue()))
                .collect(Collectors.joining(SqlClause.COMMA_SEPARATOR));
    }
}
