package org.schemawizard.core.migration;

import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

public class SW003ThirdMigration implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return null;
    }

    @Override
    public Operation down(MigrationContext context) {
        return null;
    }
}
