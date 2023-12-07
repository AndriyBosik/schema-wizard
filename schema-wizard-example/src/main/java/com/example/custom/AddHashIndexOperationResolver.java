package com.example.custom;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

@Provider(DatabaseProvider.POSTGRESQL)
public class AddHashIndexOperationResolver implements OperationResolver<AddHashIndexOperation> {
    private final OperationService operationService;

    public AddHashIndexOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(AddHashIndexOperation operation) {
        return new MigrationInfo(
                String.format(
                        "CREATE INDEX %s ON %s USING HASH(%s)",
                        operation.getName(),
                        operationService.buildTable(operation),
                        String.join(SqlClause.COLUMNS_SEPARATOR, operation.getColumns())));
    }
}
