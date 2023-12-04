package com.example.migration;

import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.builder.operation.DropColumns;
import org.schemawizard.core.migration.builder.operation.NativeQuery;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

public class SW004NativeExample implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return NativeQuery.builder()
                .file("db/migration/initial-up.sql")
                .sql("alter table public.posts add column if not exists column1 text not null")
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropColumns.builder("public", "posts")
                .columns("column0", "column1")
                .build();
    }
}
