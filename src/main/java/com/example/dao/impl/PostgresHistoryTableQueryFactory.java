package com.example.dao.impl;

import com.example.dao.HistoryTableQueryFactory;

import static com.example.dao.Constants.APPLIED_ON;
import static com.example.dao.Constants.DESCRIPTION;
import static com.example.dao.Constants.ID;
import static com.example.dao.Constants.MIGRATION_TABLE_NAME;
import static com.example.dao.Constants.VERSION;

public class PostgresHistoryTableQueryFactory implements HistoryTableQueryFactory {
    @Override
    public String getCreateMigrationHistoryTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s ("
                + "%s SERIAL PRIMARY KEY, "
                + "%s INTEGER NOT NULL, "
                + "%s TEXT NOT NULL, "
                + "%s TIMESTAMP NOT NULL DEFAULT now()"
                + ");",
                MIGRATION_TABLE_NAME, ID, VERSION, DESCRIPTION, APPLIED_ON);
    }

    @Override
    public String getSelectTableSql() {
        return String.format("SELECT table_name "
                + "FROM information_schema.tables "
                + "WHERE table_type='BASE TABLE' "
                + "AND table_name='%s';",
                MIGRATION_TABLE_NAME);
    }

    @Override
    public String getSelectMigrationsSql() {
        return String.format("SELECT %s, %s, %s, %s "
                        + "FROM %s order by version;",
                ID, VERSION, DESCRIPTION, APPLIED_ON,
                MIGRATION_TABLE_NAME);
    }
}
