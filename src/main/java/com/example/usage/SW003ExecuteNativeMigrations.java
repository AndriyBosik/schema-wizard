package com.example.usage;

import com.example.migration.Migration;
import com.example.migration.builder.operation.NativeQuery;
import com.example.migration.model.MigrationContext;
import com.example.migration.operation.Operation;

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
