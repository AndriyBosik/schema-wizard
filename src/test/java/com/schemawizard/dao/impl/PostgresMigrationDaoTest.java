package com.schemawizard.dao.impl;

import com.schemawizard.dao.MigrationDao;
import com.schemawizard.dao.entity.MigrationEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class PostgresMigrationDaoTest {
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    private static final String INSERT_MIGRATION_SQL = "INSERT INTO public.migration_history " +
            "(version, description, checksum, applied_on, success) VALUES " +
            "(?, ?, ?, ?, ?)";

    private static final String CREATE_MIGRATION_HISTORY_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS public.migration_history ("
                    + "id SERIAL PRIMARY KEY, "
                    + "version INTEGER NOT NULL, "
                    + "description TEXT NOT NULL, "
                    + "applied_on TIMESTAMP NOT NULL DEFAULT now(),"
                    + "checksum INTEGER NOT NULL, "
                    + "success BOOLEAN NOT NULL DEFAULT FALSE"
                    + ")";

    private final Connection connection = createConnection();

    private final MigrationDao migrationDao = new PostgresMigrationDao(connection);

    @BeforeAll
    static void setUp() {
        postgres.start();
    }

    @AfterAll
    static void stop() {
        postgres.stop();
    }

    private Connection createConnection() {
        try {
            return DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void migrationDaoShouldReturnMigrations() throws SQLException {
        createMigrationsTable();
        List<MigrationEntity> expectedMigrations = createExpectedMigrations();
        saveMigrationsToDb(expectedMigrations);
        var actualMigrations = migrationDao.getAllMigrations();
        assertEquals(actualMigrations.size(), expectedMigrations.size());
        for(int i = 0; i < expectedMigrations.size(); i++) {
            MigrationEntity actualMigration = actualMigrations.get(i);
            MigrationEntity expectedMigration = expectedMigrations.get(i);
            assertNotNull(actualMigration.getId());
            assertEquals(expectedMigration.getVersion(), actualMigration.getVersion());
            assertEquals(expectedMigration.getChecksum(), actualMigration.getChecksum());
            assertEquals(expectedMigration.getDescription(), actualMigration.getDescription());
            assertEquals(expectedMigration.getAppliedOn().truncatedTo(ChronoUnit.MILLIS),
                    actualMigration.getAppliedOn().truncatedTo(ChronoUnit.MILLIS));
            assertEquals(expectedMigration.isSuccess(), actualMigration.isSuccess());
        }
    }

    private void createMigrationsTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_MIGRATION_HISTORY_TABLE_SQL);
        }
    }

    private List<MigrationEntity> createExpectedMigrations() {
        List<MigrationEntity> migrations = new ArrayList<>();

        MigrationEntity migration1 = new MigrationEntity();
        migration1.setVersion(1);
        migration1.setDescription("test1");
        migration1.setChecksum(1000);
        migration1.setAppliedOn(LocalDateTime.now());
        migration1.setSuccess(true);

        MigrationEntity migration2 = new MigrationEntity();
        migration2.setVersion(2);
        migration2.setDescription("test2");
        migration2.setChecksum(2000);
        migration2.setAppliedOn(LocalDateTime.now());
        migration2.setSuccess(false);

        migrations.add(migration1);
        migrations.add(migration2);

        return migrations;
    }

    void saveMigrationsToDb(List<MigrationEntity> migrations) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_MIGRATION_SQL)) {
            for (MigrationEntity migration : migrations) {
                ps.setInt(1, migration.getVersion());
                ps.setString(2, migration.getDescription());
                ps.setInt(3, migration.getChecksum());
                ps.setTimestamp(4, Timestamp.valueOf(migration.getAppliedOn()));
                ps.setBoolean(5, migration.isSuccess());
                ps.executeUpdate();
            }
        }
    }
}
