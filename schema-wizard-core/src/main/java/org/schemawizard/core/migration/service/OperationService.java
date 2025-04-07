package org.schemawizard.core.migration.service;

import org.schemawizard.core.migration.factory.ColumnTypeFactory;
import org.schemawizard.core.migration.model.ColumnMetadata;
import org.schemawizard.core.migration.operation.AddColumnOperation;
import org.schemawizard.core.migration.operation.TableBasedOperation;

public interface OperationService {
    String buildFullName(String schema, String table);

    String buildTable(TableBasedOperation operation);

    boolean isPrimaryKeyColumn(String name, String... primaryKeyColumns);

    String mapColumnName(String name);

    String[] mapColumnNames(String... names);

    String buildColumnDefinition(AddColumnOperation operation, ColumnTypeFactory typeFactory);

    String buildColumnDefinition(AddColumnOperation operation, ColumnTypeFactory typeFactory, ColumnMetadata metadata);
}
