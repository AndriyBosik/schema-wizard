package com.example.migration;

import io.github.andriybosik.schemawizard.core.migration.Migration;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.DropColumns;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.NativeQuery;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationContext;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

public class SW004NativeExample implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return NativeQuery.builder()
                .file("db/migration/initial-up.sql")
                .sql("alter table SCHEMAWIZARD.posts add column1 text not null")
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropColumns.builder("SCHEMAWIZARD", "posts")
                .columns("column0", "column1")
                .build();
    }
}
