package org.schemawizard.core.analyzer.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.schemawizard.core.analyzer.*;
import org.schemawizard.core.analyzer.exception.MigrationAnalyzerException;
import org.schemawizard.core.analyzer.service.AppliedMigrationsService;
import org.schemawizard.core.analyzer.service.DeclaredMigrationService;
import org.schemawizard.core.analyzer.AppliedMigration;
import org.schemawizard.core.migration.SW001CreateUsersTable;
import org.schemawizard.core.migration.SW002CreatePostsTable;
import org.schemawizard.core.migration.SW003CompositeMigration;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class MigrationAnalyzerImplTest {

    private final AppliedMigrationsService appliedMigrationsService = Mockito.mock(AppliedMigrationsService.class);

    private final DeclaredMigrationService declaredMigrationsService = Mockito.mock(DeclaredMigrationService.class);

    private final HistoryTableCreator historyTableCreator = Mockito.mock(HistoryTableCreator.class);

    private final MigrationAnalyzer migrationAnalyzer = new MigrationAnalyzerImpl(
            appliedMigrationsService,
            declaredMigrationsService,
            historyTableCreator);

    @Test
    void analyzeShouldReturnNotAppliedMigrations() {
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
        var actualMigrations = migrationAnalyzer.analyze();

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
                LocalDateTime.now()
        );

        AppliedMigration migration2 = new AppliedMigration(
                2,
                2,
                "Migration Description 2",
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
                LocalDateTime.now()
        );

        AppliedMigration migration2 = new AppliedMigration(
                2,
                2,
                "SecondMigration",
                LocalDateTime.now()
        );

        AppliedMigration migration3 = new AppliedMigration(
                3,
                3,
                "ThirdMigration",
                LocalDateTime.now()
        );

        return List.of(migration1, migration2, migration3);
    }

    private List<DeclaredMigration> createThreeDeclaredMigrations() {
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

        DeclaredMigration migration3 = new DeclaredMigration(
                3,
                "ThirdMigration",
                SW003CompositeMigration.class
        );

        return List.of(migration1, migration2, migration3);
    }

    @Test
    void analyzeShouldNotReturnAnythingIfMigrationsNumbersTheSame() {
        var appliedMigrations = createTwoAppliedMigrations();
        var declaredMigrations = createTwoDeclaredMigrations();
        when(appliedMigrationsService.getAppliedMigrations()).thenReturn(appliedMigrations);
        when(declaredMigrationsService.getDeclaredMigrations()).thenReturn(declaredMigrations);

        var actualMigrations = migrationAnalyzer.analyze();
        assertNotNull(actualMigrations);
        assertEquals(0, actualMigrations.size());
    }

    @Test
    void analyseShouldThrowExceptionIfAppliedMigrationsNumberGreaterThenDeclared() {
        var appliedMigrations = createThreeAppliedMigrations();
        var declaredMigrations = createTwoDeclaredMigrations();

        when(appliedMigrationsService.getAppliedMigrations()).thenReturn(appliedMigrations);
        when(declaredMigrationsService.getDeclaredMigrations()).thenReturn(declaredMigrations);
        assertThrows(MigrationAnalyzerException.class, migrationAnalyzer::analyze);
    }

    @Test
    void analyseShouldThrowExceptionIfAppliedAndDeclaredMigrationVersionDifferent() {
        AppliedMigration appliedMigration = new AppliedMigration(
                1,
                1,
                "Migration Description 1",
                LocalDateTime.now()
        );

        DeclaredMigration declaredMigration = new DeclaredMigration(
                2,
                "Migration Description 1",
                SW002CreatePostsTable.class
        );

        when(appliedMigrationsService.getAppliedMigrations()).thenReturn(List.of(appliedMigration));
        when(declaredMigrationsService.getDeclaredMigrations()).thenReturn(List.of(declaredMigration));

        assertThrows(MigrationAnalyzerException.class, migrationAnalyzer::analyze);
    }
}
