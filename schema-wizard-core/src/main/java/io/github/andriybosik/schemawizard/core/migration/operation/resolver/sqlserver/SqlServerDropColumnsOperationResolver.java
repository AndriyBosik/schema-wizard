package io.github.andriybosik.schemawizard.core.migration.operation.resolver.sqlserver;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.metadata.SqlClause;
import io.github.andriybosik.schemawizard.core.migration.annotation.Provider;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.DropColumnsOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.resolver.OperationResolver;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;

import java.util.stream.Collectors;

@Provider(DatabaseProvider.SQLSERVER)
public class SqlServerDropColumnsOperationResolver implements OperationResolver<DropColumnsOperation> {
    private final OperationService operationService;

    public SqlServerDropColumnsOperationResolver(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public MigrationInfo resolve(DropColumnsOperation operation) {
        return new MigrationInfo(
                String.format(
                        "%s %s %s %s",
                        SqlClause.ALTER_TABLE,
                        operationService.buildTable(operation),
                        SqlClause.DROP,
                        buildDropColumnsQuery(operation)));
    }

    private String buildDropColumnsQuery(DropColumnsOperation operation) {
        return operation.getColumns().stream()
                .map(dropOperation -> String.format(
                        "%s%s %s",
                        SqlClause.COLUMN,
                        dropOperation.isIfExists() ? (" " + SqlClause.IF_EXISTS) : "",
                        operationService.mapColumnName(dropOperation.getName())))
                .collect(Collectors.joining(SqlClause.COMMA_SEPARATOR));
    }
}
