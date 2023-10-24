package com.schemawizard.dao.impl;

import com.schemawizard.dao.MigrationDao;
import com.schemawizard.dao.entity.MigrationEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostgresMigrationDao implements MigrationDao {

    private static final String SELECT_MIGRATIONS_SQL = "SELECT id, version, description, checksum, applied_on, success " +
            "FROM public.migration_history;";

    private final Connection connection;

    public PostgresMigrationDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<MigrationEntity> getAllMigrations() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(SELECT_MIGRATIONS_SQL);
            var rs = statement.getResultSet();
            List<MigrationEntity> migrations = new ArrayList<>();
            while (rs.next()) {
                migrations.add(extractMigrationFromRs(rs));
            }
            return migrations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private MigrationEntity extractMigrationFromRs(ResultSet rs) throws SQLException {
        MigrationEntity migration = new MigrationEntity();
        migration.setId(rs.getInt("id"));
        migration.setVersion(rs.getInt("version"));
        migration.setDescription(rs.getString("description"));
        migration.setChecksum(rs.getInt("checksum"));
        migration.setAppliedOn(rs.getTimestamp("applied_on").toLocalDateTime());
        migration.setSuccess(rs.getBoolean("success"));
        return migration;
    }
}
