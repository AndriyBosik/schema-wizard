package com.schemawizard.analyzer.impl;


import com.schemawizard.analyzer.HistoryTableCreator;
import com.schemawizard.analyzer.MigrationService;
import com.schemawizard.analyzer.Migration;
import com.schemawizard.analyzer.MigrationAnalyzer;
import com.schemawizard.analyzer.exception.MigrationAnalyzerException;

import java.util.List;

public class MigrationAnalyzerImpl implements MigrationAnalyzer {
    private final MigrationService tableMigrationsService;

    private final MigrationService classesMigrationsService;

    private final HistoryTableCreator historyTableCreator;

    public MigrationAnalyzerImpl(MigrationService tableMigrationsService,
                                 MigrationService classesMigrationsService,
                                 HistoryTableCreator historyTableCreator) {
        this.tableMigrationsService = tableMigrationsService;
        this.classesMigrationsService = classesMigrationsService;
        this.historyTableCreator = historyTableCreator;
    }

    @Override
    public List<Migration> analyze() {
        historyTableCreator.createTableIfNotExist();
        var appliedMigrations = tableMigrationsService.getMigrations();
        var declaredMigrations = classesMigrationsService.getMigrations();
        if (appliedMigrations.size() > declaredMigrations.size()) {
            throw new MigrationAnalyzerException("Applied migrations number is greater then all migrations number");
        }
        for (int i = 0; i < appliedMigrations.size(); i++) {
            Migration appliedMigration = appliedMigrations.get(i);
            Migration declaredMigration = declaredMigrations.get(i);
            compareAppliedAndDeclaredMigrations(appliedMigration, declaredMigration);
        }
        return declaredMigrations.subList(appliedMigrations.size(), declaredMigrations.size());
    }

    private void compareAppliedAndDeclaredMigrations(Migration appliedMigration, Migration declaredMigration) {
        if (appliedMigration.getVersion() != declaredMigration.getVersion()) {
            throw new MigrationAnalyzerException(
                    String.format("Applied migration version %d doesn't match declared migration version %d",
                            appliedMigration.getVersion(), declaredMigration.getVersion()));
        }
        if (appliedMigration.getChecksum() != declaredMigration.getChecksum()) {
            throw new MigrationAnalyzerException(
                    String.format("Applied migration checksum %d doesn't match declared migration checksum %d",
                            appliedMigration.getChecksum(), declaredMigration.getChecksum()));
        }
    }
}
