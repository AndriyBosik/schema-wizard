package io.github.andriybosik.schemawizard.core.analyzer.service.impl;

import io.github.andriybosik.schemawizard.core.analyzer.AppliedMigration;
import io.github.andriybosik.schemawizard.core.analyzer.exception.MigrationAnalyzerException;
import io.github.andriybosik.schemawizard.core.analyzer.service.AppliedMigrationsService;
import io.github.andriybosik.schemawizard.core.analyzer.service.DowngradeStrategy;
import io.github.andriybosik.schemawizard.core.dao.HistoryTableQueryFactory;
import io.github.andriybosik.schemawizard.core.dao.TransactionService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static io.github.andriybosik.schemawizard.core.dao.Constants.APPLIED_ON;
import static io.github.andriybosik.schemawizard.core.dao.Constants.CONTEXT;
import static io.github.andriybosik.schemawizard.core.dao.Constants.DESCRIPTION;
import static io.github.andriybosik.schemawizard.core.dao.Constants.ID;
import static io.github.andriybosik.schemawizard.core.dao.Constants.VERSION;

public class AppliedMigrationsServiceImpl implements AppliedMigrationsService {
    private final TransactionService transactionService;
    private final HistoryTableQueryFactory historyTableQueryFactory;

    public AppliedMigrationsServiceImpl(TransactionService transactionService, HistoryTableQueryFactory historyTableQueryFactory) {
        this.transactionService = transactionService;
        this.historyTableQueryFactory = historyTableQueryFactory;
    }

    @Override
    public List<AppliedMigration> getAppliedMigrations() {
        return transactionService.doWithinTransaction(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute(historyTableQueryFactory.getSelectMigrationsSql());
                var rs = statement.getResultSet();
                List<AppliedMigration> migrations = new ArrayList<>();
                while (rs.next()) {
                    migrations.add(extractMigrationFromRs(rs));
                }
                return migrations;
            } catch (SQLException exception) {
                throw new MigrationAnalyzerException(exception.getMessage(), exception);
            }
        });
    }

    @Override
    public List<AppliedMigration> getMigrationsByDowngradeStrategy(DowngradeStrategy strategy) {
        return strategy.apply(ps -> {
            try {
                ps.execute();
                var rs = ps.getResultSet();
                List<AppliedMigration> migrations = new ArrayList<>();
                while (rs.next()) {
                    migrations.add(extractMigrationFromRs(rs));
                }
                return migrations;
            } catch (SQLException exception) {
                throw new MigrationAnalyzerException(exception.getMessage(), exception);
            }
        });
    }

    private AppliedMigration extractMigrationFromRs(ResultSet rs) throws SQLException {
        return new AppliedMigration(
                rs.getInt(ID),
                rs.getInt(VERSION),
                rs.getString(DESCRIPTION),
                rs.getString(CONTEXT),
                rs.getTimestamp(APPLIED_ON).toLocalDateTime());
    }
}
