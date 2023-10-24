package com.example.migration;

import com.example.migration.model.MigrationContext;
import com.example.migration.operation.Operation;

public interface Migration {
    Operation up(MigrationContext context);

    Operation down(MigrationContext context);
}
