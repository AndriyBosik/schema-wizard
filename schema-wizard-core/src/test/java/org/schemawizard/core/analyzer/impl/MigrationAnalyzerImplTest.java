package org.schemawizard.core.analyzer.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.schemawizard.core.analyzer.AppliedMigration;
import org.schemawizard.core.analyzer.DeclaredMigration;
import org.schemawizard.core.analyzer.HistoryTable;
import org.schemawizard.core.analyzer.MigrationAnalyzer;
import org.schemawizard.core.analyzer.MigrationData;
import org.schemawizard.core.analyzer.exception.MigrationAnalyzerException;
import org.schemawizard.core.analyzer.factory.DowngradeFactory;
import org.schemawizard.core.analyzer.model.VersionDowngradeStrategyParameters;
import org.schemawizard.core.analyzer.service.AppliedMigrationsService;
import org.schemawizard.core.analyzer.service.DeclaredMigrationService;
import org.schemawizard.core.migration.SW001CreateUsersTable;
import org.schemawizard.core.migration.SW002CreatePostsTable;
import org.schemawizard.core.migration.SW003CompositeMigration;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class MigrationAnalyzerImplTest {

    private final AppliedMigrationsService appliedMigrationsService = Mockito.mock(AppliedMigrationsService.class);

    private final DeclaredMigrationService declaredMigrationsService = Mockito.mock(DeclaredMigrationService.class);

    private final HistoryTable historyTable = Mockito.mock(HistoryTable.class);

    private final DowngradeFactory downgradeFactory = Mockito.mock(DowngradeFactory.class);

    private final MigrationAnalyzer migrationAnalyzer = new MigrationAnalyzerImpl(
            appliedMigrationsService,
            declaredMigrationsService,
            historyTable,
            downgradeFactory);

    @Test
    void upgradeAnalyzeShouldReturnNotAppliedMigrations() {
        var appliedMigrations = createTwoAppliedMigrations();
        var declaredMigrations = createThreeDeclaredMigrations();

        when(appliedMigrationsService.getAppliedMigrations()).thenReturn(appliedMigrations);
        when(declaredMigrationsService.getDeclaredMigrations()).thenReturn(declaredMigrations);

        MigrationData thirdMigrationData = new MigrationData(
                3,
                "ThirdMigration",
                new SW003CompositeMigration()
        );

        var expectedMigrations = List.of(thirdMigrationData);
        var actualMigrations = migrationAnalyzer.upgradeAnalyze();

        assertNotNull(actualMigrations);
        assertEquals(expectedMigrations.size(), actualMigrations.size());
        for (int i = 0; i < expectedMigrations.size(); i++) {
            MigrationData expectedMigrationData = expectedMigrations.get(i);
            MigrationData actualMigrationData = actualMigrations.get(i);
            assertEquals(expectedMigrationData.getVersion(), actualMigrationData.getVersion());
        }
    }

    private List<AppliedMigration> createTwoAppliedMigrations() {
        AppliedMigration migration1 = new AppliedMigration(
                1,
                1,
                "Migration Description 1",
                "context",
                LocalDateTime.now()
        );

        AppliedMigration migration2 = new AppliedMigration(
                2,
                2,
                "Migration Description 2",
                "context",
                LocalDateTime.now()
        );
        return List.of(migration1, migration2);
    }

    private List<DeclaredMigration> createTwoDeclaredMigrations() {
        DeclaredMigration migration1 = new DeclaredMigration(
                1,
                "FirstMigration",
                SW001CreateUsersTable.class
        );

        DeclaredMigration migration2 = new DeclaredMigration(
                2,
                "SecondMigration",
                SW002CreatePostsTable.class
        );

        return List.of(migration1, migration2);
    }

    private List<AppliedMigration> createThreeAppliedMigrations() {
        AppliedMigration migration1 = new AppliedMigration(
                1,
                1,
                "FirstMigration",
                "context",
                LocalDateTime.now()
        );

        AppliedMigration migration2 = new AppliedMigration(
                2,
                2,
                "SecondMigration",
                "context",
                LocalDateTime.now()
        );

        AppliedMigration migration3 = new AppliedMigration(
                3,
                3,
                "ThirdMigration",
                "context",
                LocalDateTime.now()
        );

        return List.of(migration1, migration2, migration3);
    }

    private List<DeclaredMigration> createThreeDeclaredMigrations() {
        DeclaredMigration migration1 = new DeclaredMigration(
                1,
                "first migration",
                SW001CreateUsersTable.class
        );

        DeclaredMigration migration2 = new DeclaredMigration(
                2,
                "second migration",
                SW002CreatePostsTable.class
        );

        DeclaredMigration migration3 = new DeclaredMigration(
                3,
                "third migration",
                SW003CompositeMigration.class
        );

        return List.of(migration1, migration2, migration3);
    }

    @Test
    void upgradeAnalyzeShouldNotReturnAnythingIfMigrationsNumbersTheSame() {
        var appliedMigrations = createTwoAppliedMigrations();
        var declaredMigrations = createTwoDeclaredMigrations();
        when(appliedMigrationsService.getAppliedMigrations()).thenReturn(appliedMigrations);
        when(declaredMigrationsService.getDeclaredMigrations()).thenReturn(declaredMigrations);

        var actualMigrations = migrationAnalyzer.upgradeAnalyze();
        assertNotNull(actualMigrations);
        assertEquals(0, actualMigrations.size());
    }

    @Test
    void upgradeAnalyzeShouldThrowExceptionIfAppliedMigrationsNumberGreaterThenDeclared() {
        var appliedMigrations = createThreeAppliedMigrations();
        var declaredMigrations = createTwoDeclaredMigrations();

        when(appliedMigrationsService.getAppliedMigrations()).thenReturn(appliedMigrations);
        when(declaredMigrationsService.getDeclaredMigrations()).thenReturn(declaredMigrations);
        assertThrows(MigrationAnalyzerException.class, migrationAnalyzer::upgradeAnalyze);
    }

    @Test
    void upgradeAnalyzeShouldThrowExceptionIfAppliedAndDeclaredMigrationVersionDifferent() {
        AppliedMigration appliedMigration = new AppliedMigration(
                1,
                1,
                "Migration Description 1",
                "context",
                LocalDateTime.now()
        );

        DeclaredMigration declaredMigration = new DeclaredMigration(
                2,
                "Migration Description 1",
                SW002CreatePostsTable.class
        );

        when(appliedMigrationsService.getAppliedMigrations()).thenReturn(List.of(appliedMigration));
        when(declaredMigrationsService.getDeclaredMigrations()).thenReturn(List.of(declaredMigration));

        assertThrows(MigrationAnalyzerException.class, migrationAnalyzer::upgradeAnalyze);
    }

    @Test
    void downgradeAnalyzeShouldThrowExceptionIfHistoryTableDoesNotExist() {
        when(historyTable.exists()).thenReturn(false);
        assertThrows(MigrationAnalyzerException.class, () -> migrationAnalyzer.downgradeAnalyze(new VersionDowngradeStrategyParameters(2)));
    }

    @Test
    void downgradeAnalyzeShouldThrowExceptionIfMultipleDeclaredMigrationWithTheSameVersion() {
        var declaredMigrationsWithTheSameVersion = createDeclaredMigrationsWithTheSameVersion();
        when(declaredMigrationsService.getDeclaredMigrations()).thenReturn(declaredMigrationsWithTheSameVersion);
        assertThrows(MigrationAnalyzerException.class, () -> migrationAnalyzer.downgradeAnalyze(new VersionDowngradeStrategyParameters(2)));
    }

    private List<DeclaredMigration> createDeclaredMigrationsWithTheSameVersion() {
        DeclaredMigration migration = new DeclaredMigration(
                1,
                "first migration",
                SW001CreateUsersTable.class
        );

        DeclaredMigration migration1 = new DeclaredMigration(
                1,
                "first migration",
                SW001CreateUsersTable.class
        );


        return List.of(migration, migration1);
    }
}
