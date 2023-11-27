package org.schemawizard.core.migration.operation.resolver.oracle;

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

import java.util.stream.Collectors;

@Provider(DatabaseProvider.ORACLE)
public class OracleAddColumnsOperationResolver implements OperationResolver<AddColumnsOperation> {
    private final OperationService operationService;
    private final ColumnTypeFactory columnTypeFactory;

    public OracleAddColumnsOperationResolver(
            OperationService operationService,
            @Qualifier(ColumnTypeFactoryQualifier.ORACLE) ColumnTypeFactory columnTypeFactory
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
