package com.schemawizard.migration;

import com.example.migration.Migration;
import com.example.migration.annotation.SWName;
import com.example.migration.model.MigrationContext;
import com.example.migration.operation.Operation;

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
