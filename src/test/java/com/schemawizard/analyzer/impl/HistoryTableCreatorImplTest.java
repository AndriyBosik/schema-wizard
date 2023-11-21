package com.schemawizard.analyzer.impl;

import com.example.analyzer.HistoryTableCreator;
import com.example.analyzer.impl.HistoryTableCreatorImpl;
import com.example.dao.ConnectionHolder;
import com.example.dao.impl.PostgresHistoryTableQueryFactory;
import com.example.model.ConfigurationProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HistoryTableCreatorImplTest {

    private static final String SELECT_TABLE_SQL = "SELECT table_name " +
            "FROM information_schema.tables " +
            "WHERE table_type='BASE TABLE' AND table_name='migration_history';";
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    private final ConnectionHolder connectionHolder = new ConnectionHolder(
            ConfigurationProperties.builder()
                    .connectionUrl(postgres.getJdbcUrl())
                    .username(postgres.getUsername())
                    .password(postgres.getPassword())
                    .build()
    );

    private final HistoryTableCreator historyTableCreator = new HistoryTableCreatorImpl(
            connectionHolder, new PostgresHistoryTableQueryFactory());

    @BeforeAll
    static void setUp() {
        postgres.start();
    }

    @AfterAll
    static void stop() {
        postgres.stop();
    }

    @Test
    public void historyTableCreatorShouldCreateTable() {
        historyTableCreator.createTableIfNotExist();
        try (Statement statement = connectionHolder.getConnection().createStatement()) {
            statement.execute(SELECT_TABLE_SQL);
            assertTrue(statement.getResultSet().next());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}