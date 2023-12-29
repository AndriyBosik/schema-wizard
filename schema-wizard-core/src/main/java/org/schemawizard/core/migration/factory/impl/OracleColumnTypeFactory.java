package org.schemawizard.core.migration.factory.impl;

import org.schemawizard.core.di.annotation.Qualifier;
import org.schemawizard.core.migration.factory.ColumnTypeFactory;
import org.schemawizard.core.migration.metadata.ColumnTypeFactoryQualifier;
import org.schemawizard.core.migration.metadata.PlainColumnType;

@Qualifier(ColumnTypeFactoryQualifier.ORACLE)
public class OracleColumnTypeFactory implements ColumnTypeFactory {
    @Override
    public String getNative(String type) {
        if (PlainColumnType.TEXT.equalsIgnoreCase(type)) {
            return "VARCHAR";
        } else if (PlainColumnType.DOUBLE.equalsIgnoreCase(type)) {
            return "NUMBER";
        } else if (PlainColumnType.INTEGER.equalsIgnoreCase(type)) {
            return "INTEGER";
        } else if (PlainColumnType.BOOLEAN.equalsIgnoreCase(type)) {
            return "NUMBER(1)";
        }
        return type;
    }
}
