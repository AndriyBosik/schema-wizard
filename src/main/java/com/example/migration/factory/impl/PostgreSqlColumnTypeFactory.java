package com.example.migration.factory.impl;

import com.example.di.annotation.Qualifier;
import com.example.metadata.DatabaseProvider;
import com.example.migration.factory.ColumnTypeFactory;
import com.example.migration.metadata.ColumnTypeFactoryQualifier;
import com.example.migration.metadata.PlainColumnType;

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
