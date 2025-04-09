package io.github.andriybosik.schemawizard.core.migration.operation.resolver.mysql;

import io.github.andriybosik.schemawizard.core.di.annotation.Qualifier;
import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.factory.ColumnTypeFactory;
import io.github.andriybosik.schemawizard.core.migration.metadata.ColumnTypeFactoryQualifier;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.AddColumnsOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

import java.util.stream.Collectors;

@Provider(DatabaseProvider.MYSQL)
public class MySqlAddColumnsOperationResolver implements OperationResolver<AddColumnsOperation> {
    private final OperationService operationService;
    private final ColumnTypeFactory columnTypeFactory;

    public MySqlAddColumnsOperationResolver(
            OperationService operationService,
            @Qualifier(ColumnTypeFactoryQualifier.MYSQL) ColumnTypeFactory columnTypeFactory
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
                .collect(Collectors.joining(SqlClause.COMMA_SEPARATOR));
    }
}
