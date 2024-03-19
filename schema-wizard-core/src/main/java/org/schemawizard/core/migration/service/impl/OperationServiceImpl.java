package org.schemawizard.core.migration.service.impl;

import java.util.Optional;
import org.schemawizard.core.migration.factory.ColumnTypeFactory;
import org.schemawizard.core.migration.metadata.PlainColumnType;
import org.schemawizard.core.migration.operation.AddColumnOperation;
import org.schemawizard.core.migration.operation.TableBasedOperation;
import org.schemawizard.core.migration.service.OperationService;
import org.schemawizard.core.model.ConfigurationProperties;

public class OperationServiceImpl implements OperationService {
    private final ConfigurationProperties configurationProperties;

    public OperationServiceImpl(ConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    @Override
    public String buildTable(String schema, String table) {
        return schema == null ? table : (schema + "." + table);
    }

    @Override
    public String buildTable(TableBasedOperation operation) {
        return buildTable(operation.getSchema(), operation.getTable());
    }

    @Override
    public String buildColumnDefinition(AddColumnOperation operation,
                                        ColumnTypeFactory typeFactory) {
        StringBuilder builder = new StringBuilder(operation.getName());

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
