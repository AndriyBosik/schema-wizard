package org.schemawizard.core.old.analyzer.service.impl;

import org.schemawizard.core.analyzer.DeclaredMigration;
import org.schemawizard.core.analyzer.service.DeclaredMigrationService;
import org.schemawizard.core.analyzer.service.impl.ClassesDeclaredMigrationService;
import org.schemawizard.core.old.migration.SW001CreateUsersTable;
import org.schemawizard.core.old.migration.SW002CreatePostsTable;
import org.schemawizard.core.old.migration.SW003CompositeMigration;
import org.schemawizard.core.old.migration.inner.SW004NativeExample;
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

//    @Test
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
                "create users table",
                SW001CreateUsersTable.class
        );

        DeclaredMigration migration2 = new DeclaredMigration(
                2,
                "some long custom migration description",
                SW002CreatePostsTable.class
        );

        DeclaredMigration migration3 = new DeclaredMigration(
                3,
                "composite migration",
                SW003CompositeMigration.class
        );

        DeclaredMigration migration4 = new DeclaredMigration(
                4,
                "native example",
                SW004NativeExample.class
        );

        return List.of(migration1, migration2, migration3, migration4);
    }
}
