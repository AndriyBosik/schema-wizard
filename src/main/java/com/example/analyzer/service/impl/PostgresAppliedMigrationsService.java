package com.example.analyzer.service.impl;

import com.example.analyzer.AppliedMigration;
import com.example.analyzer.exception.MigrationAnalyzerException;
import com.example.analyzer.service.AppliedMigrationsService;
import com.example.dao.ConnectionHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostgresAppliedMigrationsService implements AppliedMigrationsService {

    private static final String SELECT_MIGRATIONS_SQL = "SELECT id, version, description, applied_on " +
            "FROM migration_history;";

    private final ConnectionHolder connectionHolder;

    public PostgresAppliedMigrationsService(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    @Override
    public List<AppliedMigration> getAppliedMigrations() {
        try (Statement statement = connectionHolder.getConnection().createStatement()) {
            statement.execute(SELECT_MIGRATIONS_SQL);
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
