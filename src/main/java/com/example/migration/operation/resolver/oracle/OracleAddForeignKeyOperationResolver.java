package com.example.migration.operation.resolver.oracle;

import com.example.metadata.DatabaseProvider;
import com.example.metadata.SqlClause;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.AddForeignKeyOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.migration.service.OperationService;

@Provider(DatabaseProvider.ORACLE)
public class OracleAddForeignKeyOperationResolver implements OperationResolver<AddForeignKeyOperation> {
    private final OperationService operationService;

    public OracleAddForeignKeyOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(AddForeignKeyOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s %s %s (%s) %s %s (%s)",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        SqlClause.ADD_CONSTRAINT,
                        operation.getName(),
                        SqlClause.FOREIGN_KEY,
                        String.join(SqlClause.COLUMNS_SEPARATOR, operation.getColumns()),
                        SqlClause.REFERENCES,
                        operationService.buildTable(operation.getForeignSchema(), operation.getForeignTable()),
                        String.join(SqlClause.COLUMNS_SEPARATOR, operation.getForeignColumns())));
    }
}
