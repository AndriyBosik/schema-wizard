package org.schemawizard.core.analyzer.service.impl;

import org.schemawizard.core.analyzer.AppliedMigration;
import org.schemawizard.core.analyzer.exception.MigrationAnalyzerException;
import org.schemawizard.core.analyzer.service.AppliedMigrationsService;
import org.schemawizard.core.dao.ConnectionHolder;
import org.schemawizard.core.dao.HistoryTableQueryFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.schemawizard.core.dao.Constants.APPLIED_ON;
import static org.schemawizard.core.dao.Constants.DESCRIPTION;
import static org.schemawizard.core.dao.Constants.ID;
import static org.schemawizard.core.dao.Constants.VERSION;

public class AppliedMigrationsServiceImpl implements AppliedMigrationsService {

    private final ConnectionHolder connectionHolder;
    private final HistoryTableQueryFactory historyTableQueryFactory;

    public AppliedMigrationsServiceImpl(ConnectionHolder connectionHolder, HistoryTableQueryFactory historyTableQueryFactory) {
        this.connectionHolder = connectionHolder;
        this.historyTableQueryFactory = historyTableQueryFactory;
    }

    @Override
    public List<AppliedMigration> getAppliedMigrations() {
        try (Statement statement = connectionHolder.getConnection().createStatement()) {
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
    }

    @Override
    public List<AppliedMigration> getMigrationsStartedFrom(Integer downgradeMigrationVersion) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement(historyTableQueryFactory.getSelectMigrationsStartedFromSql())) {
            ps.setInt(1, downgradeMigrationVersion);
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
    }

    private AppliedMigration extractMigrationFromRs(ResultSet rs) throws SQLException {
        return new AppliedMigration(
                rs.getInt(ID),
                rs.getInt(VERSION),
                rs.getString(DESCRIPTION),
                rs.getTimestamp(APPLIED_ON).toLocalDateTime());
    }
}
