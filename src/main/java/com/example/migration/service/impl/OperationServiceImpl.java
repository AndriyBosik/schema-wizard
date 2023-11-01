package com.example.migration.service.impl;

import com.example.migration.operation.AddColumnOperation;
import com.example.migration.operation.TableBasedOperation;
import com.example.migration.service.OperationService;

public class OperationServiceImpl implements OperationService {
    @Override
    public String buildTable(String schema, String table) {
        return schema == null ? table : (schema + "." + table);
    }

    @Override
    public String buildTable(TableBasedOperation operation) {
        return buildTable(operation.getSchema(), operation.getTable());
    }

    @Override
    public String buildColumnDefinition(AddColumnOperation operation) {
        StringBuilder builder = new StringBuilder(operation.getName());

        builder.append(" ").append(operation.getType());
        if (operation.getMinLength() != null || operation.getMaxLength() != null) {
            builder.append("(");
            if (operation.getMinLength() != null) {
                builder.append(operation.getMinLength());
            }
            if (operation.getMinLength() != null && operation.getMaxLength() != null) {
                builder.append(",");
            }
            if (operation.getMaxLength() != null) {
                builder.append(operation.getMaxLength());
            }
            builder.append(")");
        }
        if (operation.getPrecision() != null) {
            builder.append("(");
            builder.append(operation.getPrecision());
            if (operation.getScale() != null) {
                builder.append(",").append(operation.getScale());
            }
            builder.append(")");
        }

        builder.append(operation.isNullable() ? " NULL" : " NOT NULL");

        if (operation.getSqlDefault() != null) {
            builder.append(" DEFAULT ").append(operation.getSqlDefault());
        }

        return builder.toString();
    }
}
