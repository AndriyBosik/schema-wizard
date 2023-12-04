package org.schemawizard.core.starter;

import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.runner.MigrationRunner;

import java.util.List;

public class SchemaWizard {
    private final MigrationRunner migrationRunner;

    public SchemaWizard(MigrationRunner migrationRunner) {
        this.migrationRunner = migrationRunner;
    }

    public void up() {
        migrationRunner.upgrade();
    }
}
