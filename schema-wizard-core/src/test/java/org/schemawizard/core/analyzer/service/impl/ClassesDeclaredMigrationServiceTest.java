package org.schemawizard.core.analyzer.service.impl;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.analyzer.DeclaredMigration;
import org.schemawizard.core.analyzer.service.DeclaredMigrationService;
import org.schemawizard.core.migration.SW001FirstMigration;
import org.schemawizard.core.migration.SW002SecondMigration;
import org.schemawizard.core.migration.SW003ThirdMigration;
import org.schemawizard.core.migration.inner.SW004ForthMigration;
import org.schemawizard.core.model.ConfigurationProperties;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassesDeclaredMigrationServiceTest {

    private final DeclaredMigrationService classesDeclaredMigrationService
            = new ClassesDeclaredMigrationService(
            ConfigurationProperties.builder()
                    .migrationsPackage("org.schemawizard.core.migration")
                    .build()
    );

    @Test
    void serviceShouldReturnCorrectMigrations() {
        var actualMigrations = classesDeclaredMigrationService.getDeclaredMigrations();
        var expectedMigrations = expectedMigrations();

        assertEquals(actualMigrations.size(), expectedMigrations.size());
        for (int i = 0; i < actualMigrations.size(); i++) {
            DeclaredMigration expectedMigration = expectedMigrations.get(i);
            DeclaredMigration actualMigration = actualMigrations.get(i);
            assertEquals(expectedMigration.getVersion(), actualMigration.getVersion());
            assertEquals(expectedMigration.getDescription(), actualMigration.getDescription());
            assertEquals(expectedMigration.getMigrationClass(), actualMigration.getMigrationClass());
        }
    }

    private List<DeclaredMigration> expectedMigrations() {
        DeclaredMigration migration1 = new DeclaredMigration(
                1,
                "first migration",
                SW001FirstMigration.class
        );

        DeclaredMigration migration2 = new DeclaredMigration(
                2,
                "some long custom migration description",
                SW002SecondMigration.class
        );

        DeclaredMigration migration3 = new DeclaredMigration(
                3,
                "third migration",
                SW003ThirdMigration.class
        );

        DeclaredMigration migration4 = new DeclaredMigration(
                4,
                "forth migration",
                SW004ForthMigration.class
        );

        return List.of(migration1, migration2, migration3, migration4);
    }
}
