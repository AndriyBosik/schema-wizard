package com.schemawizard.analyzer.service.impl;

import com.example.analyzer.DeclaredMigration;
import com.example.analyzer.service.DeclaredMigrationService;
import com.example.analyzer.service.impl.ClassesDeclaredMigrationService;
import com.example.model.ConfigurationProperties;
import com.schemawizard.migration.SW001FirstMigration;
import com.schemawizard.migration.SW002SecondMigration;
import com.schemawizard.migration.SW003ThirdMigration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassesDeclaredMigrationServiceTest {

    private final DeclaredMigrationService classesDeclaredMigrationService
            = new ClassesDeclaredMigrationService(
            ConfigurationProperties.builder()
                    .migrationsPackage("com.schemawizard.migration")
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

        return List.of(migration1, migration2, migration3);
    }
}
