package com.example.migration.factory;

@FunctionalInterface
public interface ColumnTypeFactory {
    String getNative(String type);
}
