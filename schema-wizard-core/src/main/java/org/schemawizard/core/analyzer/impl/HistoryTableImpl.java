package org.schemawizard.core.analyzer.impl;

import org.schemawizard.core.analyzer.HistoryTable;
import org.schemawizard.core.analyzer.exception.MigrationAnalyzerException;
import org.schemawizard.core.dao.HistoryTableQueryFactory;
import org.schemawizard.core.dao.TransactionService;
import org.schemawizard.core.metadata.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;

import static org.schemawizard.core.dao.Constants.MIGRATION_TABLE_NAME;

public class HistoryTableImpl implements HistoryTable {
    private final Logger log = LoggerFactory.getLogger(HistoryTableImpl.class);
    private final TransactionService transactionService;
    private final HistoryTableQueryFactory historyTableQueryFactory;

    public HistoryTableImpl(TransactionService transactionService, HistoryTableQueryFactory historyTableQueryFactory) {
        this.transactionService = transactionService;
        this.historyTableQueryFactory = historyTableQueryFactory;
    }

    @Override
    public void createIfNotExists() {
        transactionService.doWithinTransaction(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute(historyTableQueryFactory.getAcquireAdvisoryLockSql());
                statement.execute(historyTableQueryFactory.getSelectTableSql());
                if (!statement.getResultSet().next()) {
                    statement.execute(historyTableQueryFactory.getCreateMigrationHistoryTableSql());
                    log.info("Migration history table created.");
                } else {
                    log.info("Migration history table already exists.");
                }
                statement.execute(historyTableQueryFactory.getReleaseAdvisoryLockSql());
            } catch (SQLException e) {
                throw new MigrationAnalyzerException("Error creating migration history table: " + e.getMessage(), e);
            }
        });
    }

    @Override
    public boolean exists() {
        return transactionService.doWithinTransaction(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute(historyTableQueryFactory.getSelectTableSql());
                return statement.getResultSet().next();
            } catch (SQLException e) {
                throw new MigrationAnalyzerException(e.getMessage(), e);
            }
        });
    }

    @Override
    public void lockForExecution() {
        transactionService.doWithinTransaction(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(historyTableQueryFactory.getLockForExecutionSql());
            } catch (SQLException exception) {
                throw new MigrationAnalyzerException(
                        String.format(
                                ErrorMessage.UNABLE_TO_LOCK_TABLE_TEMPLATE,
                                MIGRATION_TABLE_NAME));
            }
        });
    }
}
