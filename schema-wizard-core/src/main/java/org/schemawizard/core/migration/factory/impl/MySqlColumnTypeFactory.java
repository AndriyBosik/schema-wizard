package org.schemawizard.core.migration.factory.impl;

import org.schemawizard.core.di.annotation.Qualifier;
import org.schemawizard.core.migration.factory.ColumnTypeFactory;
import org.schemawizard.core.migration.metadata.ColumnTypeFactoryQualifier;
import org.schemawizard.core.migration.metadata.PlainColumnType;

@Qualifier(ColumnTypeFactoryQualifier.MYSQL)
public class MySqlColumnTypeFactory implements ColumnTypeFactory {
    @Override
    public String getNative(String type) {
        if (PlainColumnType.TEXT.equalsIgnoreCase(type)) {
            return "VARCHAR";
        } else if (PlainColumnType.DECIMAL.equalsIgnoreCase(type)) {
            return "DECIMAL";
        } else if (PlainColumnType.INTEGER.equalsIgnoreCase(type)) {
            return "INTEGER";
        } else if (PlainColumnType.BOOLEAN.equalsIgnoreCase(type)) {
            return "BOOLEAN";
        } else if (PlainColumnType.DATE.equalsIgnoreCase(type)) {
            return "DATE";
        } else if (PlainColumnType.TIMESTAMP.equalsIgnoreCase(type)) {
            return "TIMESTAMP";
        } else if (PlainColumnType.TIMESTAMP_WITH_TIME_ZONE.equalsIgnoreCase(type)) {
            return "TIMESTAMP";
        }
        return type;
    }
}
