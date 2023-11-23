package org.schemawizard.core.usage;

import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.builder.operation.NativeQuery;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

public class SW003ExecuteNativeMigrations implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return NativeQuery.builder()
                .file("db/migration/initial-up.sql")
                .sql("ALTER TABLE example.users ADD COLUMN IF NOT EXISTS enabled BOOLEAN NOT NULL DEFAULT false")
                .sql("UPDATE example.users SET enabled = true")
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return NativeQuery.builder()
                .sql("ALTER TABLE example.users DROP COLUMN IF EXISTS enabled")
                .file("db/migration/initial-down.sql")
                .build();
    }
}
