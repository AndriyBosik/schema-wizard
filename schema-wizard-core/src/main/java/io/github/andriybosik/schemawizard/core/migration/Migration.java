package io.github.andriybosik.schemawizard.core.migration;

import io.github.andriybosik.schemawizard.core.migration.model.MigrationContext;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

public interface Migration {
    Operation up(MigrationContext context);

    Operation down(MigrationContext context);
}
