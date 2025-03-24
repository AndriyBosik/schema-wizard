package org.schemawizard.core.migration.operation.resolver.oracle;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.SqlClause;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.DropColumnOperation;
import org.schemawizard.core.migration.operation.DropColumnsOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.migration.service.OperationService;

import java.util.stream.Collectors;

@Provider(DatabaseProvider.ORACLE)
public class OracleDropColumnsOperationResolver implements OperationResolver<DropColumnsOperation> {
    private final OperationService operationService;

    public OracleDropColumnsOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropColumnsOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s (%s)",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        SqlClause.DROP,
                        buildCommaSeparatedColumns(operation)));
    }

    private String buildCommaSeparatedColumns(DropColumnsOperation operation) {
        return operation.getColumns().stream()
                .map(DropColumnOperation::getName)
                .map(operationService::mapColumnName)
                .collect(Collectors.joining(SqlClause.COMMA_SEPARATOR));
    }
}
