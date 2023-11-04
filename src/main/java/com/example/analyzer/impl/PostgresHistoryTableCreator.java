package com.example.analyzer.impl;

import com.example.analyzer.HistoryTableCreator;
import com.example.analyzer.exception.MigrationAnalyzerException;
import com.example.dao.ConnectionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;

public class PostgresHistoryTableCreator implements HistoryTableCreator {

    private final Logger log = LoggerFactory.getLogger(PostgresHistoryTableCreator.class);

    private final ConnectionHolder connectionHolder;

    private static final String CREATE_MIGRATION_HISTORY_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS migration_history ("
                    + "id SERIAL PRIMARY KEY, "
                    + "version INTEGER NOT NULL, "
                    + "description TEXT NOT NULL, "
                    + "applied_on TIMESTAMP NOT NULL DEFAULT now(),"
                    + "checksum INTEGER NOT NULL, "
                    + "success BOOLEAN NOT NULL DEFAULT FALSE"
                    + ");";
    private static final String SELECT_TABLE_SQL = "SELECT table_name " +
            "FROM information_schema.tables " +
            "WHERE table_type='BASE TABLE' AND table_name='migration_history';";

    public PostgresHistoryTableCreator(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    @Override
    public void createTableIfNotExist() {
        try (Statement statement = connectionHolder.getConnection().createStatement()) {
            statement.execute(SELECT_TABLE_SQL);
            if (!statement.getResultSet().next()) {
                statement.execute(CREATE_MIGRATION_HISTORY_TABLE_SQL);
                log.info("Migration history table created.");
            } else {
                log.info("Migration history table already exists.");
            }
        } catch (SQLException e) {
            throw new MigrationAnalyzerException("Error creating migration history table: " + e.getMessage(), e);
        }
    }
}
