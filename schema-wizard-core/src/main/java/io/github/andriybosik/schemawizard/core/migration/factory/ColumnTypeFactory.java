package io.github.andriybosik.schemawizard.core.migration.factory;

@FunctionalInterface
public interface ColumnTypeFactory {
    String getNative(String type);
}
