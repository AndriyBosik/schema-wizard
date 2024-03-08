package org.schemawizard.core.migration.builder.column;

import org.schemawizard.core.migration.operation.AddColumnOperation;

public interface ColumnBuilder {
    AddColumnOperation build();
}
