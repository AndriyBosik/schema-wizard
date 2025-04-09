package io.github.andriybosik.schemawizard.core.migration.builder.column;

import io.github.andriybosik.schemawizard.core.migration.operation.AddColumnOperation;

public interface ColumnBuilder {
    ColumnBuilder name(String name);

    AddColumnOperation build();
}
