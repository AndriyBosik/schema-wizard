package com.schemawizard.analyzer.impl;

import com.schemawizard.analyzer.Migration;
import com.schemawizard.analyzer.MigrationService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassesMigrationServiceTest {

    private final MigrationService classesMigrationService
            = new ClassesMigrationService("com.schemawizard.migration");

    @Test
    void serviceShouldReturnCorrectMigrations() {
        var actualMigrations = classesMigrationService.getMigrations();
        var expectedMigrations = expectedMigrations();

        assertEquals(actualMigrations.size(), expectedMigrations.size());
        for (int i = 0; i < actualMigrations.size(); i++) {
            Migration expectedMigration = expectedMigrations.get(i);
            Migration actualMigration = actualMigrations.get(i);
            assertEquals(actualMigration.getVersion(), expectedMigration.getVersion());
            assertEquals(actualMigration.getDescription(), expectedMigration.getDescription());
            assertEquals(actualMigration.getChecksum(), expectedMigration.getChecksum());
        }
    }

    private List<Migration> expectedMigrations() {
        Migration migration1 = new Migration();
        migration1.setVersion(1);
        migration1.setDescription("FirstMigration");
        migration1.setChecksum(1);

        Migration migration2 = new Migration();
        migration2.setVersion(2);
        migration2.setDescription("SecondMigration");
        migration2.setChecksum(2);

        return List.of(migration1, migration2);
    }
}
