package org.schemawizard.core.migration.operation.resolver.mysql;

import org.schemawizard.core.di.annotation.Qualifier;
import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.factory.ColumnTypeFactory;
import org.schemawizard.core.migration.metadata.ColumnTypeFactoryQualifier;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.AddColumnOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

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
