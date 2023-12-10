package org.schemawizard.core.starter;

import org.schemawizard.core.analyzer.MigrationAnalyzer;
import org.schemawizard.core.runner.MigrationRunner;

public class SchemaWizard {
    private final MigrationRunner migrationRunner;
    private final MigrationAnalyzer migrationAnalyzer;

    public SchemaWizard(MigrationRunner migrationRunner, MigrationAnalyzer migrationAnalyzer) {
        this.migrationRunner = migrationRunner;
        this.migrationAnalyzer = migrationAnalyzer;
    }

    public void up() {
        var upgradeMigrations = migrationAnalyzer.analyze();
        migrationRunner.upgrade(upgradeMigrations);
    }
}
