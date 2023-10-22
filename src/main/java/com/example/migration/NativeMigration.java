package com.example.migration;

import com.example.Migration;
import com.example.builder.migration.NativeQueryMigrationBuilder;
import com.example.builder.model.MigrationContext;
import com.example.builder.operation.Operation;

public class NativeMigration implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return NativeQueryMigrationBuilder.builder()
                .file("db/migration/initial-up.sql")
                .sql("ALTER TABLE example.users ADD COLUMN IF NOT EXISTS enabled BOOLEAN NOT NULL DEFAULT false")
                .sql("UPDATE example.users SET enabled = true")
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return NativeQueryMigrationBuilder.builder()
                .sql("ALTER TABLE example.users DROP COLUMN IF EXISTS enabled")
                .file("db/migration/initial-down.sql")
                .build();
    }
}
