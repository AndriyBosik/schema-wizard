package org.schemawizard.core.migration.service.impl;

import org.schemawizard.core.migration.factory.ColumnTypeFactory;
import org.schemawizard.core.migration.metadata.PlainColumnType;
import org.schemawizard.core.migration.operation.AddColumnOperation;
import org.schemawizard.core.migration.operation.TableBasedOperation;
import org.schemawizard.core.migration.service.ColumnNamingStrategyService;
import org.schemawizard.core.migration.service.OperationService;
import org.schemawizard.core.model.ConfigurationProperties;

import java.util.Arrays;
import java.util.Optional;

public class OperationServiceImpl implements OperationService {
    private final ConfigurationProperties configurationProperties;
    private final ColumnNamingStrategyService columnNamingStrategyService;

    public OperationServiceImpl(
            ConfigurationProperties configurationProperties,
            ColumnNamingStrategyService columnNamingStrategyService
    ) {
        this.configurationProperties = configurationProperties;
        this.columnNamingStrategyService = columnNamingStrategyService;
    }

    @Override
    public String buildTable(String schema, String table) {
        return schema == null ? table : (schema + "." + table);
    }

    @Override
    public String mapColumnName(String name) {
        return columnNamingStrategyService.map(name);
    }

    @Override
    public String[] mapColumnNames(String... names) {
        return names == null ? new String[0] : Arrays.stream(names)
                .map(columnNamingStrategyService::map)
                .toArray(String[]::new);
    }

    @Override
    public String buildTable(TableBasedOperation operation) {
        return buildTable(operation.getSchema(), operation.getTable());
    }

    @Override
    public String buildColumnDefinition(
            AddColumnOperation operation,
            ColumnTypeFactory typeFactory
    ) {
        String columnName = columnNamingStrategyService.map(operation.getName());

        StringBuilder builder = new StringBuilder(columnName);

        builder.append(" ").append(typeFactory.getNative(operation.getType()));

        Integer maxLength = Optional.ofNullable(operation.getMaxLength())
                .orElse(Optional.ofNullable(configurationProperties.getDefaults().getText().getDefaultLength())
                        .filter(x -> operation.getType().equals(PlainColumnType.TEXT))
                        .orElse(null));

        if (operation.getMinLength() != null || maxLength != null) {
            builder.append("(");
            if (operation.getMinLength() != null) {
                builder.append(operation.getMinLength());
            }
            if (operation.getMinLength() != null && maxLength != null) {
                builder.append(",");
            }
            if (maxLength != null) {
                builder.append(maxLength);
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
