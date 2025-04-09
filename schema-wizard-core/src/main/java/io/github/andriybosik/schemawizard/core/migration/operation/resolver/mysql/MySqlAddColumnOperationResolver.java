package io.github.andriybosik.schemawizard.core.migration.operation.resolver.mysql;

import io.github.andriybosik.schemawizard.core.di.annotation.Qualifier;
import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.factory.ColumnTypeFactory;
import io.github.andriybosik.schemawizard.core.migration.metadata.ColumnTypeFactoryQualifier;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.AddColumnOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.MYSQL)
public class MySqlAddColumnOperationResolver implements OperationResolver<AddColumnOperation> {
    private final OperationService operationService;
    private final ColumnTypeFactory columnTypeFactory;

    public MySqlAddColumnOperationResolver(
            OperationService operationService,
            @Qualifier(ColumnTypeFactoryQualifier.MYSQL) ColumnTypeFactory columnTypeFactory
    ) {
        this.operationService = operationService;
        this.columnTypeFactory = columnTypeFactory;
    }

    @Override
    public MigrationInfo resolve(AddColumnOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s %s",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        SqlClause.ADD_COLUMN,
                        operationService.buildColumnDefinition(operation, columnTypeFactory)));
    }
}
