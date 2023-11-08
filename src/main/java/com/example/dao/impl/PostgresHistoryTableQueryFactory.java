package com.example.dao.impl;

import com.example.dao.HistoryTableQueryFactory;

public class PostgresHistoryTableQueryFactory implements HistoryTableQueryFactory {
    @Override
    public String getCreateMigrationHistoryTableSql() {
        return "CREATE TABLE IF NOT EXISTS migration_history ("
                + "id SERIAL PRIMARY KEY, "
                + "version INTEGER NOT NULL, "
                + "description TEXT NOT NULL, "
                + "applied_on TIMESTAMP NOT NULL DEFAULT now(),"
                + "checksum INTEGER NOT NULL, "
                + "success BOOLEAN NOT NULL DEFAULT FALSE"
                + ");";
    }

    @Override
    public String getSelectTableSql() {
        return "SELECT table_name " +
                "FROM information_schema.tables " +
                "WHERE table_type='BASE TABLE' AND table_name='migration_history';";
    }

    @Override
    public String getSelectMigrationsSql() {
        return "SELECT id, version, description, applied_on " +
                "FROM migration_history order by version;";
    }
}
