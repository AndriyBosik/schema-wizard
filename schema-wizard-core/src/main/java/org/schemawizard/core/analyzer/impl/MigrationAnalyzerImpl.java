package org.schemawizard.core.analyzer.impl;


import org.schemawizard.core.analyzer.AppliedMigration;
import org.schemawizard.core.analyzer.DeclaredMigration;
import org.schemawizard.core.analyzer.HistoryTable;
import org.schemawizard.core.analyzer.MigrationAnalyzer;
import org.schemawizard.core.analyzer.MigrationData;
import org.schemawizard.core.analyzer.exception.MigrationAnalyzerException;
import org.schemawizard.core.analyzer.factory.DowngradeFactory;
import org.schemawizard.core.analyzer.model.DowngradeStrategyParameters;
import org.schemawizard.core.analyzer.service.AppliedMigrationsService;
import org.schemawizard.core.analyzer.service.DeclaredMigrationService;
import org.schemawizard.core.analyzer.service.DowngradeStrategy;
import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MigrationAnalyzerImpl implements MigrationAnalyzer {
    private final Logger log = LoggerFactory.getLogger(MigrationAnalyzerImpl.class);
    private final AppliedMigrationsService appliedMigrationsService;
    private final DeclaredMigrationService declaredMigrationService;
    private final HistoryTable historyTable;
    private final DowngradeFactory downgradeFactory;

    public MigrationAnalyzerImpl(
            AppliedMigrationsService appliedMigrationsService,
            DeclaredMigrationService declaredMigrationService,
            HistoryTable historyTable,
            DowngradeFactory downgradeFactory
    ) {
        this.appliedMigrationsService = appliedMigrationsService;
        this.declaredMigrationService = declaredMigrationService;
        this.historyTable = historyTable;
        this.downgradeFactory = downgradeFactory;
    }

    @Override
    public List<MigrationData> upgradeAnalyze() {
        historyTable.lockForExecution();
        var appliedMigrations = appliedMigrationsService.getAppliedMigrations();
        Map<Integer, DeclaredMigration> versionDeclaredMigrationsMap = createVersionDeclaredMigrationMap();
        for (AppliedMigration migration : appliedMigrations) {
            if (!versionDeclaredMigrationsMap.containsKey(migration.getVersion())) {
                throw new MigrationAnalyzerException("Migration with version " + migration.getVersion() + " was not found");
            }
            versionDeclaredMigrationsMap.remove(migration.getVersion());
        }

        return versionDeclaredMigrationsMap.values().stream()
                .sorted(Comparator.comparing(DeclaredMigration::getVersion))
                .map(this::declaredMigrationToMigrationData)
                .collect(Collectors.toList());
    }

    @Override
    public List<MigrationData> downgradeAnalyze(DowngradeStrategyParameters parameters) {
        if (!historyTable.exists()) {
            throw new MigrationAnalyzerException("No migrations has been found, run upgrade first");
        }
        historyTable.lockForExecution();
        DowngradeStrategy strategy = downgradeFactory.getInstance(parameters);
        var appliedMigrationsToDowngrade = appliedMigrationsService.getMigrationsByDowngradeStrategy(strategy);
        if (appliedMigrationsToDowngrade.isEmpty()) {
            log.warn(strategy.getNoMigrationsFoundMessage());
            return Collections.emptyList();
        }
        Map<Integer, DeclaredMigration> declaredMigrationsMap = createVersionDeclaredMigrationMap();
        return appliedMigrationsToDowngrade.stream()
                .map(appliedMigration ->
                        Optional.ofNullable(declaredMigrationsMap.get(appliedMigration.getVersion()))
                                .orElseThrow(() -> new MigrationAnalyzerException(
                                        "Declared migration with version " + appliedMigration.getVersion() + " was not found")))
                .map(this::declaredMigrationToMigrationData)
                .collect(Collectors.toList());
    }

    private Map<Integer, DeclaredMigration> createVersionDeclaredMigrationMap() {
        return declaredMigrationService.getDeclaredMigrations()
                .stream()
                .collect(Collectors.toMap(
                        DeclaredMigration::getVersion,
                        Function.identity(),
                        (first, second) -> {
                            throw new MigrationAnalyzerException("Multiple migrations with version " + first.getVersion() + " were found");
                        }));
    }

    private MigrationData declaredMigrationToMigrationData(DeclaredMigration declaredMigration) {
        var migrationClass = declaredMigration.getMigrationClass();
        checkDbMigrationClass(migrationClass);
        Migration dbMigration = ReflectionUtils.invokeConstructor(migrationClass);
        return new MigrationData(
                declaredMigration.getVersion(),
                declaredMigration.getDescription(),
                dbMigration
        );
    }

    private void checkDbMigrationClass(Class<? extends Migration> dbMigrationClass) {
        var constructors = dbMigrationClass.getDeclaredConstructors();
        if (constructors.length != 1) {
            throw new MigrationAnalyzerException(dbMigrationClass.getName() + " should have only one public constructor without params");
        }
        if (!Modifier.isPublic(constructors[0].getModifiers())) {
            throw new MigrationAnalyzerException(dbMigrationClass.getName() + " constructor should be 'public'");
        }
        if (constructors[0].getParameters().length != 0) {
            throw new MigrationAnalyzerException(dbMigrationClass.getName() + " constructor shouldn't have params");
        }
    }
}
