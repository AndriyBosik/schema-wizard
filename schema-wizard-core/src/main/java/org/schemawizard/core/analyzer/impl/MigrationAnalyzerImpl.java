package org.schemawizard.core.analyzer.impl;


import org.schemawizard.core.analyzer.*;
import org.schemawizard.core.analyzer.exception.MigrationAnalyzerException;
import org.schemawizard.core.analyzer.service.AppliedMigrationsService;
import org.schemawizard.core.analyzer.service.DeclaredMigrationService;
import org.schemawizard.core.analyzer.AppliedMigration;
import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.utils.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MigrationAnalyzerImpl implements MigrationAnalyzer {
    private final AppliedMigrationsService appliedMigrationsService;
    private final DeclaredMigrationService declaredMigrationService;
    private final HistoryTableCreator historyTableCreator;

    public MigrationAnalyzerImpl(AppliedMigrationsService appliedMigrationsService,
                                 DeclaredMigrationService declaredMigrationService,
                                 HistoryTableCreator historyTableCreator) {
        this.appliedMigrationsService = appliedMigrationsService;
        this.declaredMigrationService = declaredMigrationService;
        this.historyTableCreator = historyTableCreator;
    }

    @Override
    public List<MigrationData> analyze() {
        historyTableCreator.createTableIfNotExist();
        var appliedMigrations = appliedMigrationsService.getAppliedMigrations();
        Map<Integer, DeclaredMigration> declaredMigrationsMap = declaredMigrationService.getDeclaredMigrations()
                .stream()
                .collect(Collectors.toMap(
                                DeclaredMigration::getVersion,
                                Function.identity(),
                                (first, second) -> {
                                    throw new MigrationAnalyzerException("Multiple migrations with version " + first.getVersion() + " were found");
                                }));
        for (AppliedMigration migration: appliedMigrations) {
            if (!declaredMigrationsMap.containsKey(migration.getVersion())) {
                throw new MigrationAnalyzerException("Migration with version " + migration.getVersion() + " was not found");
            }
            declaredMigrationsMap.remove(migration.getVersion());
        }

        return declaredMigrationsMap.values().stream()
                .sorted(Comparator.comparing(DeclaredMigration::getVersion))
                .map(this::declaredMigrationToMigrationData)
                .collect(Collectors.toList());
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
        if(!Modifier.isPublic(constructors[0].getModifiers())) {
            throw new MigrationAnalyzerException(dbMigrationClass.getName() + " constructor should be 'public'");
        }
        if(constructors[0].getParameters().length != 0) {
            throw new MigrationAnalyzerException(dbMigrationClass.getName() + " constructor shouldn't have params");
        }
    }
}
