package org.schemawizard.core.migration;

import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

public interface Migration {
    Operation up(MigrationContext context);

    Operation down(MigrationContext context);
}
