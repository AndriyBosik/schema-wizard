package io.github.andriybosik.schemawizard.core.migration.factory.impl;

import io.github.andriybosik.schemawizard.core.di.annotation.Qualifier;
import io.github.andriybosik.schemawizard.core.migration.factory.ColumnTypeFactory;
import io.github.andriybosik.schemawizard.core.migration.metadata.ColumnTypeFactoryQualifier;
import io.github.andriybosik.schemawizard.core.migration.metadata.PlainColumnType;

@Qualifier(ColumnTypeFactoryQualifier.SQLSERVER)
public class SqlServerColumnTypeFactory implements ColumnTypeFactory {
    @Override
    public String getNative(String type) {
        if (PlainColumnType.TEXT.equalsIgnoreCase(type)) {
            return "VARCHAR";
        } else if (PlainColumnType.DECIMAL.equalsIgnoreCase(type)) {
            return "DECIMAL";
        } else if (PlainColumnType.INTEGER.equalsIgnoreCase(type)) {
            return "INT";
        } else if (PlainColumnType.BOOLEAN.equalsIgnoreCase(type)) {
            return "BIT";
        } else if (PlainColumnType.DATE.equalsIgnoreCase(type)) {
            return "DATE";
        } else if (PlainColumnType.TIMESTAMP.equalsIgnoreCase(type)) {
            return "DATETIME";
        } else if (PlainColumnType.TIMESTAMP_WITH_TIME_ZONE.equalsIgnoreCase(type)) {
            return "DATETIMEOFFSET";
        }
        return type;
    }
}
