package org.schemawizard.core.migration.factory.impl;

import org.schemawizard.core.di.annotation.Qualifier;
import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.migration.factory.ColumnTypeFactory;
import org.schemawizard.core.migration.metadata.ColumnTypeFactoryQualifier;
import org.schemawizard.core.migration.metadata.PlainColumnType;

@Qualifier(ColumnTypeFactoryQualifier.POSTGRESQL)
public class PostgreSqlColumnTypeFactory implements ColumnTypeFactory {
    @Override
    public String getNative(String type) {
        if (PlainColumnType.TEXT.equalsIgnoreCase(type)) {
            return "VARCHAR";
        } else if (PlainColumnType.DOUBLE.equalsIgnoreCase(type)) {
            return "DOUBLE";
        } else if (PlainColumnType.INTEGER.equalsIgnoreCase(type)) {
            return "INTEGER";
        } else if (PlainColumnType.BOOLEAN.equalsIgnoreCase(type)) {
            return "BOOLEAN";
        }
        return type;
    }
}
