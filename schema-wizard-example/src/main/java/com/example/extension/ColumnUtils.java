package com.example.extension;

import org.schemawizard.core.migration.builder.column.GenericColumnBuilder;
import org.schemawizard.core.migration.factory.ColumnBuilderFactory;

public class ColumnUtils {
    public static GenericColumnBuilder json(ColumnBuilderFactory factory) {
        return factory.generic()
                .type("json")
                .nullable(false)
                .sqlDefault("JSON_BUILD_OBJECT()");
    }
}
