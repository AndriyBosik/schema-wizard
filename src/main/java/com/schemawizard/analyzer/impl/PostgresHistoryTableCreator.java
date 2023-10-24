package com.schemawizard.analyzer.impl;

import com.schemawizard.analyzer.HistoryTableCreator;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class PostgresHistoryTableCreator implements HistoryTableCreator {

    private final Connection connection;

    private static final String CREATE_MIGRATION_HISTORY_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS public.migration_history ("
                    + "id SERIAL PRIMARY KEY, "
                    + "version INTEGER NOT NULL, "
                    + "description TEXT NOT NULL, "
                    + "applied_on TIMESTAMP NOT NULL DEFAULT now(),"
                    + "checksum INTEGER NOT NULL, "
                    + "success BOOLEAN NOT NULL DEFAULT FALSE"
                    + ");";
    private static final String SELECT_TABLE_SQL = "SELECT table_name " +
            "FROM information_schema.tables " +
            "WHERE table_schema='public' AND table_type='BASE TABLE' AND table_name='migration_history';";

    public PostgresHistoryTableCreator(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createTableIfNotExist() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(SELECT_TABLE_SQL);
            if (!statement.getResultSet().next()) {
                statement.execute(CREATE_MIGRATION_HISTORY_TABLE_SQL);
                log.info("Migration history table created.");
            } else {
                log.info("Migration history table already exists.");
            }
        } catch (SQLException e) {
            log.error("Error creating migration history table: " + e.getMessage());
        }
    }
}
