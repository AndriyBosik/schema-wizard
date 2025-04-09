package io.github.andriybosik.schemawizard.core.migration.factory.impl;

import io.github.andriybosik.schemawizard.core.di.annotation.Qualifier;
import io.github.andriybosik.schemawizard.core.migration.factory.ColumnTypeFactory;
import io.github.andriybosik.schemawizard.core.migration.metadata.ColumnTypeFactoryQualifier;
import io.github.andriybosik.schemawizard.core.migration.metadata.PlainColumnType;

@Qualifier(ColumnTypeFactoryQualifier.POSTGRESQL)
public class PostgreSqlColumnTypeFactory implements ColumnTypeFactory {
    @Override
    public String getNative(String type) {
        if (PlainColumnType.TEXT.equalsIgnoreCase(type)) {
            return "VARCHAR";
        } else if (PlainColumnType.DECIMAL.equalsIgnoreCase(type)) {
            return "NUMERIC";
        } else if (PlainColumnType.INTEGER.equalsIgnoreCase(type)) {
            return "INTEGER";
        } else if (PlainColumnType.BOOLEAN.equalsIgnoreCase(type)) {
            return "BOOLEAN";
        } else if (PlainColumnType.DATE.equalsIgnoreCase(type)) {
            return "DATE";
        } else if (PlainColumnType.TIMESTAMP.equalsIgnoreCase(type)) {
            return "TIMESTAMP";
        } else if (PlainColumnType.TIMESTAMP_WITH_TIME_ZONE.equalsIgnoreCase(type)) {
            return "TIMESTAMP WITH TIME ZONE";
        }
        return type;
    }
}
