package com.example.analyzer.service.impl;

import com.example.analyzer.AppliedMigration;
import com.example.analyzer.exception.MigrationAnalyzerException;
import com.example.analyzer.service.AppliedMigrationsService;
import com.example.dao.ConnectionHolder;
import com.example.dao.HistoryTableQueryFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
        } catch (SQLException e) {
            throw new MigrationAnalyzerException(e.getMessage(), e);
        }
    }

    private AppliedMigration extractMigrationFromRs(ResultSet rs) throws SQLException {

        return new AppliedMigration(
                rs.getInt("id"),
                rs.getInt("version"),
                rs.getString("description"),
                rs.getTimestamp("applied_on").toLocalDateTime());
    }
}
