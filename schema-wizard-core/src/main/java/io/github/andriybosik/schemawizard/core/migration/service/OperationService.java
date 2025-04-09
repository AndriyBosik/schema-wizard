package io.github.andriybosik.schemawizard.core.migration.service;

import io.github.andriybosik.schemawizard.core.migration.factory.ColumnTypeFactory;
import io.github.andriybosik.schemawizard.core.migration.model.ColumnMetadata;
import io.github.andriybosik.schemawizard.core.migration.operation.AddColumnOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.TableBasedOperation;

public interface OperationService {
    String buildFullName(String schema, String table);

    String buildTable(TableBasedOperation operation);

    boolean isPrimaryKeyColumn(String name, String... primaryKeyColumns);

    String mapColumnName(String name);

    String[] mapColumnNames(String... names);

    String buildColumnDefinition(AddColumnOperation operation, ColumnTypeFactory typeFactory);

    String buildColumnDefinition(AddColumnOperation operation, ColumnTypeFactory typeFactory, ColumnMetadata metadata);
}
