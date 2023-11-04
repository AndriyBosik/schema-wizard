package com.schemawizard.migration;

import com.example.migration.Migration;
import com.example.migration.model.MigrationContext;
import com.example.migration.operation.Operation;

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
