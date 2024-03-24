package org.schemawizard.core.analyzer.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.schemawizard.core.analyzer.HistoryTable;
import org.schemawizard.core.dao.ConnectionHolder;
import org.schemawizard.core.dao.TransactionService;
import org.schemawizard.core.dao.impl.PostgresHistoryTableQueryFactory;
import org.schemawizard.core.dao.impl.TransactionServiceImpl;
import org.schemawizard.core.model.ConfigurationProperties;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HistoryTableImplTest {

    private static final String SELECT_TABLE_SQL = "SELECT table_name " +
            "FROM information_schema.tables " +
            "WHERE table_type='BASE TABLE' AND table_name='migration_history';";
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    private final ConnectionHolder connectionHolder = new ConnectionHolder(
            ConfigurationProperties.builder()
                    .connectionUrl(postgres.getJdbcUrl())
                    .username(postgres.getUsername())
                    .password(postgres.getPassword())
                    .build());

    private final TransactionService transactionService = new TransactionServiceImpl(connectionHolder);

    private final HistoryTable historyTable = new HistoryTableImpl(
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
    @Order(1)
    void isHistoryTableExistShouldReturnFalseIfTableNotExist() {
        assertFalse(historyTable.exists());
    }

    @Test
    @Order(2)
    void createTableIfNotExistShouldCreateTable() {
        historyTable.createIfNotExists();
        try (Statement statement = connectionHolder.getConnection().createStatement()) {
            statement.execute(SELECT_TABLE_SQL);
            assertTrue(statement.getResultSet().next());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    void isHistoryTableExistShouldReturnTrueIfTableExist() {
        assertTrue(historyTable.exists());
    }
}
