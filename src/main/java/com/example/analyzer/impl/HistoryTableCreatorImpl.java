package com.example.analyzer.impl;

import com.example.analyzer.HistoryTableCreator;
import com.example.analyzer.exception.MigrationAnalyzerException;
import com.example.dao.ConnectionHolder;
import com.example.dao.HistoryTableQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;

public class HistoryTableCreatorImpl implements HistoryTableCreator {

    private final Logger log = LoggerFactory.getLogger(HistoryTableCreatorImpl.class);
    private final ConnectionHolder connectionHolder;
    private final HistoryTableQueryFactory historyTableQueryFactory;

    public HistoryTableCreatorImpl(ConnectionHolder connectionHolder, HistoryTableQueryFactory historyTableQueryFactory) {
        this.connectionHolder = connectionHolder;
        this.historyTableQueryFactory = historyTableQueryFactory;
    }

    @Override
    public void createTableIfNotExist() {
        try (Statement statement = connectionHolder.getConnection().createStatement()) {
            statement.execute(historyTableQueryFactory.getSelectTableSql());
            if (!statement.getResultSet().next()) {
                statement.execute(historyTableQueryFactory.getCreateMigrationHistoryTableSql());
                log.info("Migration history table created.");
            } else {
                log.info("Migration history table already exists.");
            }
        } catch (SQLException e) {
            throw new MigrationAnalyzerException("Error creating migration history table: " + e.getMessage(), e);
        }
    }
}
