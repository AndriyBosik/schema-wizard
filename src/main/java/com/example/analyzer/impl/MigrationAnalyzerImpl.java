package com.example.analyzer.impl;


import com.example.analyzer.*;
import com.example.analyzer.exception.MigrationAnalyzerException;
import com.example.analyzer.service.DeclaredMigrationService;
import com.example.migration.Migration;
import com.example.analyzer.service.AppliedMigrationsService;
import com.example.analyzer.AppliedMigration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;
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
        var declaredMigrations = declaredMigrationService.getDeclaredMigrations();
        if (appliedMigrations.size() > declaredMigrations.size()) {
            throw new MigrationAnalyzerException("Applied migrations number is greater then declared migrations number");
        }
        for (int i = 0; i < appliedMigrations.size(); i++) {
            AppliedMigration appliedMigration = appliedMigrations.get(i);
            DeclaredMigration declaredMigration = declaredMigrations.get(i);
            compareAppliedAndDeclaredMigrations(appliedMigration, declaredMigration);
        }

        return declaredMigrations.subList(appliedMigrations.size(), declaredMigrations.size())
                .stream()
                .map(this::declaredMigrationToMigrationData)
                .collect(Collectors.toList());
    }

    private void compareAppliedAndDeclaredMigrations(AppliedMigration appliedMigration, DeclaredMigration declaredMigration) {
        if (appliedMigration.getVersion() != declaredMigration.getVersion()) {
            throw new MigrationAnalyzerException(
                    String.format("Applied migration version %d doesn't match declared migration version %d",
                            appliedMigration.getVersion(), declaredMigration.getVersion()));
        }
    }

    private MigrationData declaredMigrationToMigrationData(DeclaredMigration declaredMigration) {
        var migrationClass = declaredMigration.getMigrationClass();
        checkDbMigrationClass(migrationClass);
        try {
            Migration dbMigration = migrationClass.getConstructor().newInstance();
            return new MigrationData(
                    declaredMigration.getVersion(),
                    declaredMigration.getDescription(),
                    dbMigration
            );
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new MigrationAnalyzerException(e.getMessage(), e);
        }
    }

    private void checkDbMigrationClass(Class<? extends Migration> dbMigrationClass) {
        var constructors = dbMigrationClass.getDeclaredConstructors();
        if (constructors.length != 1) {
            throw new MigrationAnalyzerException(dbMigrationClass.getName() + "should have only one public constructor without params");
        }
        if(!Modifier.isPublic(constructors[0].getModifiers())) {
            throw new MigrationAnalyzerException(dbMigrationClass.getName() + "constructor should be 'public'");
        }
        if(constructors[0].getParameters().length != 0) {
            throw new MigrationAnalyzerException(dbMigrationClass.getName() + "constructor shouldn't have params");
        }
    }
}
