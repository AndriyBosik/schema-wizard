package org.schemawizard.core.analyzer.service;

import org.schemawizard.core.analyzer.AppliedMigration;

import java.util.List;

public interface AppliedMigrationsService {
    List<AppliedMigration> getAppliedMigrations();

    List<AppliedMigration> getMigrationsStartedFromOrderByIdDesc(int downgradeMigrationVersion);
}
