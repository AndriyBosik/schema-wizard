package org.schemawizard.core.migration.service;

import org.schemawizard.core.migration.factory.ColumnTypeFactory;
import org.schemawizard.core.migration.operation.AddColumnOperation;
import org.schemawizard.core.migration.operation.TableBasedOperation;

public interface OperationService {
    String buildTable(String schema, String table);

    String buildTable(TableBasedOperation operation);

    String mapColumnName(String name);

    String[] mapColumnNames(String... names);

    String buildColumnDefinition(AddColumnOperation operation, ColumnTypeFactory typeFactory);
}
