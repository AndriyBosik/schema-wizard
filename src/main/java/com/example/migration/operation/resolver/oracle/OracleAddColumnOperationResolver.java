package com.example.migration.operation.resolver.oracle;

import com.example.di.annotation.Qualifier;
import com.example.metadata.DatabaseProvider;
import com.example.metadata.SqlClause;
import com.example.migration.annotation.Provider;
import com.example.migration.factory.ColumnTypeFactory;
import com.example.migration.metadata.ColumnTypeFactoryQualifier;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.AddColumnOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

@Provider(DatabaseProvider.ORACLE)
public class OracleAddColumnOperationResolver implements OperationResolver<AddColumnOperation> {
    private final OperationService operationService;
    private final ColumnTypeFactory columnTypeFactory;

    public OracleAddColumnOperationResolver(
            OperationService operationService,
            @Qualifier(ColumnTypeFactoryQualifier.ORACLE) ColumnTypeFactory columnTypeFactory
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
