package io.github.andriybosik.schemawizard.core.analyzer.service;

import io.github.andriybosik.schemawizard.core.analyzer.AppliedMigration;

import java.util.List;

public interface AppliedMigrationsService {
    List<AppliedMigration> getAppliedMigrations();

    List<AppliedMigration> getMigrationsByDowngradeStrategy(DowngradeStrategy strategy);
}
