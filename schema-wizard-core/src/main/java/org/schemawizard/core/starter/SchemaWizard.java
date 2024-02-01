package org.schemawizard.core.starter;

import org.schemawizard.core.analyzer.MigrationAnalyzer;
import org.schemawizard.core.analyzer.model.ContextDowngradeStrategyParameters;
import org.schemawizard.core.analyzer.model.VersionDowngradeStrategyParameters;
import org.schemawizard.core.runner.MigrationRunner;

public class SchemaWizard {
    private final MigrationRunner migrationRunner;
    private final MigrationAnalyzer migrationAnalyzer;

    public SchemaWizard(MigrationRunner migrationRunner, MigrationAnalyzer migrationAnalyzer) {
        this.migrationRunner = migrationRunner;
        this.migrationAnalyzer = migrationAnalyzer;
    }

    public void up() {
        var upgradeMigrations = migrationAnalyzer.upgradeAnalyze();
        migrationRunner.upgrade(upgradeMigrations);
    }

    public void down(int version) {
        var downgradeMigrations = migrationAnalyzer.downgradeAnalyze(new VersionDowngradeStrategyParameters(version));
        migrationRunner.downgrade(downgradeMigrations);
    }

    public void down(String context) {
        var downgradeMigrations = migrationAnalyzer.downgradeAnalyze(new ContextDowngradeStrategyParameters(context));
        migrationRunner.downgrade(downgradeMigrations);
    }
}
