package com.example.custom;

import org.schemawizard.core.migration.builder.column.ColumnBuilder;
import org.schemawizard.core.migration.factory.ColumnBuilderFactory;

import java.util.function.Function;

public class ColumnUtils {
    public static Function<ColumnBuilderFactory, ColumnBuilder> json(String name) {
        return factory -> factory.generic(name)
                .nullable(false)
                .type("json");
    }
}
