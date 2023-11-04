package com.example.migration.service;

import com.example.migration.factory.ColumnTypeFactory;
import com.example.migration.operation.AddColumnOperation;
import com.example.migration.operation.TableBasedOperation;

public interface OperationService {
    String buildTable(String schema, String table);

    String buildTable(TableBasedOperation operation);

    String buildColumnDefinition(AddColumnOperation operation, ColumnTypeFactory typeFactory);
}
