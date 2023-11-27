package org.schemawizard.core.migration.factory;

@FunctionalInterface
public interface ColumnTypeFactory {
    String getNative(String type);
}
