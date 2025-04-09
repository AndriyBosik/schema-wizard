package io.github.andriybosik.schemawizard.core.migration.service.impl;

import io.github.andriybosik.schemawizard.core.migration.factory.ColumnTypeFactory;
import io.github.andriybosik.schemawizard.core.migration.metadata.PlainColumnType;
import io.github.andriybosik.schemawizard.core.migration.model.ColumnMetadata;
import io.github.andriybosik.schemawizard.core.migration.operation.AddColumnOperation;
import io.github.andriybosik.schemawizard.core.migration.operation.TableBasedOperation;
import io.github.andriybosik.schemawizard.core.migration.service.ColumnNamingStrategyService;
import io.github.andriybosik.schemawizard.core.migration.service.OperationService;
import io.github.andriybosik.schemawizard.core.model.ConfigurationProperties;

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
    public String buildFullName(String schema, String table) {
        return schema == null ? table : (schema + "." + table);
    }

    @Override
    public String buildTable(TableBasedOperation operation) {
        return buildFullName(operation.getSchema(), operation.getTable());
    }

    @Override
    public boolean isPrimaryKeyColumn(String name, String... primaryKeyColumns) {
        return Arrays.stream(mapColumnNames(primaryKeyColumns))
                .anyMatch(x -> x.equals(mapColumnName(name)));
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
    public String buildColumnDefinition(
            AddColumnOperation operation,
            ColumnTypeFactory typeFactory
    ) {
        return buildColumnDefinition(operation, typeFactory, new ColumnMetadata(true));
    }


    @Override
    public String buildColumnDefinition(AddColumnOperation operation, ColumnTypeFactory typeFactory, ColumnMetadata metadata) {
        String columnName = mapColumnName(operation.getName());

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

        if (operation.getSqlDefault() != null) {
            builder.append(" DEFAULT ").append(operation.getSqlDefault());
        }

        builder.append(operation.isNullable() && metadata.isNullAllowed() ? " NULL" : " NOT NULL");

        return builder.toString();
    }
}
