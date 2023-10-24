package com.schemawizard.analyzer.impl;

import com.schemawizard.analyzer.HistoryTableCreator;
import com.schemawizard.analyzer.Migration;
import com.schemawizard.analyzer.MigrationAnalyzer;
import com.schemawizard.analyzer.MigrationService;
import com.schemawizard.analyzer.exception.MigrationAnalyzerException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class MigrationAnalyzerImplTest {

    private final MigrationService tableMigrationsService = Mockito.mock(MigrationService.class);

    private final MigrationService classesMigrationsService = Mockito.mock(MigrationService.class);

    private final HistoryTableCreator historyTableCreator = Mockito.mock(HistoryTableCreator.class);

    private final MigrationAnalyzer migrationAnalyzer = new MigrationAnalyzerImpl(
            tableMigrationsService,
            classesMigrationsService,
            historyTableCreator);

    @Test
    void analyzeShouldReturnNotAppliedMigrations() {
        var appliedMigrations = createTwoMigrations();
        var declaredMigrations = createThreeMigrations();

        when(tableMigrationsService.getMigrations()).thenReturn(appliedMigrations);
        when(classesMigrationsService.getMigrations()).thenReturn(declaredMigrations);

        var expectedMigrations = declaredMigrations.subList(appliedMigrations.size(), declaredMigrations.size());
        var actualMigrations = migrationAnalyzer.analyze();

        assertNotNull(actualMigrations);
        assertEquals(expectedMigrations.size(), actualMigrations.size());
        for (int i = 0; i < expectedMigrations.size(); i++) {
            Migration expectedMigration = expectedMigrations.get(i);
            Migration actualMigration = actualMigrations.get(i);
            assertEquals(expectedMigration.getVersion(), actualMigration.getVersion());
            assertEquals(expectedMigration.getChecksum(), actualMigration.getChecksum());
        }
    }

    private List<Migration> createTwoMigrations() {
        Migration migration1 = new Migration();
        migration1.setVersion(1);
        migration1.setDescription("Migration Description 1");
        migration1.setChecksum(12345);

        Migration migration2 = new Migration();
        migration2.setVersion(2);
        migration2.setDescription("Migration Description 2");
        migration2.setChecksum(54321);

        return List.of(migration1, migration2);
    }

    private List<Migration> createThreeMigrations() {
        Migration migration1 = new Migration();
        migration1.setVersion(1);
        migration1.setDescription("Migration Description 1");
        migration1.setChecksum(12345);

        Migration migration2 = new Migration();
        migration2.setVersion(2);
        migration2.setDescription("Migration Description 2");
        migration2.setChecksum(54321);

        Migration migration3 = new Migration();
        migration3.setVersion(3);
        migration3.setDescription("Migration Description 3");
        migration3.setChecksum(56789);

        return List.of(migration1, migration2, migration3);
    }

    @Test
    void analyzeShouldNotReturnAnythingIfMigrationsNumbersTheSame() {
        var appliedMigrations = createTwoMigrations();
        var declaredMigrations = createTwoMigrations();
        when(tableMigrationsService.getMigrations()).thenReturn(appliedMigrations);
        when(classesMigrationsService.getMigrations()).thenReturn(declaredMigrations);

        var actualMigrations = migrationAnalyzer.analyze();
        assertNotNull(actualMigrations);
        assertEquals(0, actualMigrations.size());
    }

    @Test
    void analyseShouldThrowExceptionIfAppliedMigrationsNumberGreaterThenDeclared() {
        var appliedMigrations = createThreeMigrations();
        var declaredMigrations = createTwoMigrations();

        when(tableMigrationsService.getMigrations()).thenReturn(appliedMigrations);
        when(classesMigrationsService.getMigrations()).thenReturn(declaredMigrations);
        assertThrows(MigrationAnalyzerException.class, migrationAnalyzer::analyze);
    }

    @Test
    void analyseShouldThrowExceptionIfAppliedAndDeclaredMigrationVersionDifferent() {
        Migration appliedMigration = new Migration();
        appliedMigration.setVersion(1);
        appliedMigration.setDescription("Migration Description 1");
        appliedMigration.setChecksum(12345);

        Migration declaredMigration = new Migration();
        declaredMigration.setVersion(2);
        declaredMigration.setDescription("Migration Description 1");
        declaredMigration.setChecksum(12345);

        when(tableMigrationsService.getMigrations()).thenReturn(List.of(appliedMigration));
        when(classesMigrationsService.getMigrations()).thenReturn(List.of(declaredMigration));

        assertThrows(MigrationAnalyzerException.class, migrationAnalyzer::analyze);
    }

    @Test
    void analyseShouldThrowExceptionIfAppliedAndDeclaredMigrationChecksumDifferent() {
        Migration appliedMigration = new Migration();
        appliedMigration.setVersion(1);
        appliedMigration.setDescription("Migration Description 1");
        appliedMigration.setChecksum(12345);

        Migration declaredMigration = new Migration();
        declaredMigration.setVersion(1);
        declaredMigration.setDescription("Migration Description 1");
        declaredMigration.setChecksum(56789);

        when(tableMigrationsService.getMigrations()).thenReturn(List.of(appliedMigration));
        when(classesMigrationsService.getMigrations()).thenReturn(List.of(declaredMigration));

        assertThrows(MigrationAnalyzerException.class, migrationAnalyzer::analyze);
    }
}
