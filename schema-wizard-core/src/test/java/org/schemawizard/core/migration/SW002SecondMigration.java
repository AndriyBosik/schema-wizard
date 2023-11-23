package org.schemawizard.core.migration;

import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.annotation.SWName;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

@SWName("some long custom migration description")
public class SW002SecondMigration implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return null;
    }

    @Override
    public Operation down(MigrationContext context) {
        return null;
    }
}
