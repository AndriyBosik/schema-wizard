package org.schemawizard.core.dao.impl;

import org.schemawizard.core.dao.HistoryTableQueryFactory;

import static org.schemawizard.core.dao.Constants.APPLIED_ON;
import static org.schemawizard.core.dao.Constants.CONTEXT;
import static org.schemawizard.core.dao.Constants.DESCRIPTION;
import static org.schemawizard.core.dao.Constants.ID;
import static org.schemawizard.core.dao.Constants.MIGRATION_TABLE_NAME;
import static org.schemawizard.core.dao.Constants.VERSION;

public class PostgresHistoryTableQueryFactory implements HistoryTableQueryFactory {
    @Override
    public String getCreateMigrationHistoryTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s ("
                        + "%s SERIAL PRIMARY KEY, "
                        + "%s INTEGER NOT NULL, "
                        + "%s TEXT NOT NULL, "
                        + "%s TEXT, "
                        + "%s TIMESTAMP NOT NULL DEFAULT now(), "
                        + "CONSTRAINT unq_%s_%s UNIQUE (%s))",
                MIGRATION_TABLE_NAME,
                ID,
                VERSION,
                DESCRIPTION,
                CONTEXT,
                APPLIED_ON,
                MIGRATION_TABLE_NAME, VERSION, VERSION);
    }

    @Override
    public String getSelectTableSql() {
        return String.format("SELECT table_name "
                        + "FROM information_schema.tables "
                        + "WHERE table_type='BASE TABLE' "
                        + "AND table_name='%s'",
                MIGRATION_TABLE_NAME);
    }

    @Override
    public String getSelectMigrationsSql() {
        return String.format("SELECT %s, %s, %s, %s, %s "
                        + "FROM %s order by version",
                ID, VERSION, DESCRIPTION, CONTEXT, APPLIED_ON,
                MIGRATION_TABLE_NAME);
    }

    @Override
    public String getSelectMigrationsStartedFromSqlOrderByIdDesc() {
        return String.format("WITH break_row AS MATERIALIZED (" +
                        "  SELECT %s FROM %s WHERE %s = ?)" +
                        "SELECT mh.%s, mh.%s, mh.%s, mh.%s, mh.%s " +
                        "FROM %s mh " +
                        "LEFT JOIN break_row ON TRUE " +
                        "WHERE break_row.%s IS NOT NULL AND %s >= break_row.%s " +
                        "ORDER BY %s DESC",
                ID, MIGRATION_TABLE_NAME, VERSION,
                ID, VERSION, DESCRIPTION, CONTEXT, APPLIED_ON,
                MIGRATION_TABLE_NAME,
                ID, ID, ID,
                ID);
    }

    @Override
    public String getSelectLastMigrationsByContext() {
        return String.format(
                "WITH break_row AS MATERIALIZED (" +
                        " SELECT * FROM %s WHERE %s != ? ORDER BY %s DESC LIMIT 1) " +
                        "SELECT mh.%s, mh.%s, mh.%s, mh.%s, mh.%s " +
                        "FROM %s mh " +
                        "LEFT JOIN break_row ON TRUE " +
                        "WHERE mh.%s = ? AND (break_row.%s IS NULL OR mh.%s > break_row.%s) " +
                        "ORDER BY mh.%s DESC",
                MIGRATION_TABLE_NAME, CONTEXT, ID,
                ID, VERSION, DESCRIPTION, CONTEXT, APPLIED_ON,
                MIGRATION_TABLE_NAME,
                CONTEXT, ID, ID, ID,
                ID);
    }

    @Override
    public String getInsertMigrationHistoryRowQuery() {
        return String.format(
                "INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)",
                MIGRATION_TABLE_NAME,
                VERSION,
                DESCRIPTION,
                CONTEXT);
    }

    @Override
    public String getDeleteMigrationHistoryRowQuery() {
        return String.format(
                "DELETE FROM %s WHERE %s = ?",
                MIGRATION_TABLE_NAME,
                VERSION);
    }
}
