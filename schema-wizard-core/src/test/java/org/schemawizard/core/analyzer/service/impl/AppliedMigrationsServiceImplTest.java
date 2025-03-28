package org.schemawizard.core.analyzer.service.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.schemawizard.core.analyzer.AppliedMigration;
import org.schemawizard.core.analyzer.service.AppliedMigrationsService;
import org.schemawizard.core.dao.ConnectionHolder;
import org.schemawizard.core.dao.TransactionService;
import org.schemawizard.core.dao.impl.DriverLoaderImpl;
import org.schemawizard.core.dao.impl.PostgresHistoryTableQueryFactory;
import org.schemawizard.core.dao.impl.TransactionServiceImpl;
import org.schemawizard.core.model.ConfigurationProperties;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AppliedMigrationsServiceImplTest {
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    private static final String INSERT_MIGRATION_SQL = "INSERT INTO migration_history " +
            "(version, description, applied_on) VALUES " +
            "(?, ?, ?)";

    private static final String CREATE_MIGRATION_HISTORY_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS migration_history ("
                    + "id SERIAL PRIMARY KEY, "
                    + "version INTEGER NOT NULL, "
                    + "description TEXT NOT NULL, "
                    + "context TEXT, "
                    + "applied_on TIMESTAMP NOT NULL DEFAULT now()"
                    + ")";

    private final ConnectionHolder connectionHolder = new ConnectionHolder(
            ConfigurationProperties.builder()
                    .connectionUrl(postgres.getJdbcUrl())
                    .username(postgres.getUsername())
                    .password(postgres.getPassword())
                    .build(),
            new DriverLoaderImpl(
                    Thread.currentThread().getContextClassLoader()));

    private final TransactionService transactionService = new TransactionServiceImpl(connectionHolder);

    private final AppliedMigrationsService appliedMigrationsService = new AppliedMigrationsServiceImpl(
            transactionService, new PostgresHistoryTableQueryFactory());

    @BeforeAll
    static void setUp() {
        postgres.start();
    }

    @AfterAll
    static void stop() {
        postgres.stop();
    }

    @Test
    void getAppliedMigrationsShouldReturnAllMigrations() throws SQLException {
        createMigrationsTable();
        List<AppliedMigration> expectedMigrations = createAllMigrations();
        saveMigrationsToDb(expectedMigrations);
        var actualMigrations = appliedMigrationsService.getAppliedMigrations();
        assertEquals(actualMigrations.size(), expectedMigrations.size());
        for (int i = 0; i < expectedMigrations.size(); i++) {
            AppliedMigration actualMigration = actualMigrations.get(i);
            AppliedMigration expectedMigration = expectedMigrations.get(i);
            assertNotNull(actualMigration.getId());
            assertEquals(expectedMigration.getVersion(), actualMigration.getVersion());
            assertEquals(expectedMigration.getDescription(), actualMigration.getDescription());
            assertEquals(expectedMigration.getAppliedOn().truncatedTo(ChronoUnit.MILLIS),
                    actualMigration.getAppliedOn().truncatedTo(ChronoUnit.MILLIS));
        }
        deleteMigrationTable();
    }

    private void createMigrationsTable() throws SQLException {
        try (Statement statement = connectionHolder.getConnection().createStatement()) {
            statement.execute(CREATE_MIGRATION_HISTORY_TABLE_SQL);
        }
    }

    private List<AppliedMigration> createAllMigrations() {
        List<AppliedMigration> migrations = new ArrayList<>();

        AppliedMigration migration1 = new AppliedMigration(
                1,
                1,
                "test1",
                "context",
                LocalDateTime.now()
        );

        AppliedMigration migration2 = new AppliedMigration(
                2,
                2,
                "test2",
                "context",
                LocalDateTime.now()
        );

        AppliedMigration migration3 = new AppliedMigration(
                3,
                3,
                "test3",
                "context",
                LocalDateTime.now()
        );

        AppliedMigration migration4 = new AppliedMigration(
                4,
                4,
                "test4",
                "context",
                LocalDateTime.now()
        );

        migrations.add(migration1);
        migrations.add(migration2);
        migrations.add(migration3);
        migrations.add(migration4);

        return migrations;
    }

    private void saveMigrationsToDb(List<AppliedMigration> migrations) throws SQLException {
        try (PreparedStatement ps = connectionHolder.getConnection().prepareStatement(INSERT_MIGRATION_SQL)) {
            for (AppliedMigration migration : migrations) {
                ps.setInt(1, migration.getVersion());
                ps.setString(2, migration.getDescription());
                ps.setTimestamp(3, Timestamp.valueOf(migration.getAppliedOn()));
                ps.executeUpdate();
            }
        }
    }

    private void deleteMigrationTable() throws SQLException {
        try (Statement st = connectionHolder.getConnection().createStatement()) {
            st.executeUpdate("DROP TABLE IF EXISTS migration_history");
        }
    }
}
