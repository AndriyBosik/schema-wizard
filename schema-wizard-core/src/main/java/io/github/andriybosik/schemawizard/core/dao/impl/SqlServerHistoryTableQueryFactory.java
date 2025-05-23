package io.github.andriybosik.schemawizard.core.dao.impl;

import io.github.andriybosik.schemawizard.core.dao.HistoryTableQueryFactory;

import static io.github.andriybosik.schemawizard.core.dao.Constants.APPLIED_ON;
import static io.github.andriybosik.schemawizard.core.dao.Constants.CONTEXT;
import static io.github.andriybosik.schemawizard.core.dao.Constants.DESCRIPTION;
import static io.github.andriybosik.schemawizard.core.dao.Constants.ID;
import static io.github.andriybosik.schemawizard.core.dao.Constants.MIGRATION_TABLE_NAME;
import static io.github.andriybosik.schemawizard.core.dao.Constants.VERSION;

public class SqlServerHistoryTableQueryFactory implements HistoryTableQueryFactory {
    private static final String ADVISORY_LOCK_NAME = "SCHEMAWIZARD_LOCK";
    private static final String ADVISORY_LOCK_OWNER = "Session";
    private static final String METADATA_TABLE_NAME = "information_schema.tables";

    @Override
    public String getCreateMigrationHistoryTableSql() {
        return String.format("CREATE TABLE %s ("
                        + "%s INT IDENTITY (1, 1) PRIMARY KEY, "
                        + "%s INT NOT NULL, "
                        + "%s VARCHAR(1023) NOT NULL, "
                        + "%s VARCHAR(1023), "
                        + "%s DATETIME NOT NULL DEFAULT GETDATE(), "
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
                        + "FROM %s "
                        + "WHERE table_type='BASE TABLE' "
                        + "AND table_name='%s' "
                        + "AND table_schema = schema_name()",
                METADATA_TABLE_NAME,
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
        return String.format("WITH break_row (%s) AS (" +
                        "  SELECT %s FROM %s WHERE %s = ?)" +
                        "SELECT mh.%s, mh.%s, mh.%s, mh.%s, mh.%s " +
                        "FROM %s mh " +
                        "LEFT JOIN break_row ON (1 = 1) " +
                        "WHERE break_row.%s IS NOT NULL AND mh.%s >= break_row.%s " +
                        "ORDER BY mh.%s DESC",
                ID,
                ID, MIGRATION_TABLE_NAME, VERSION,
                ID, VERSION, DESCRIPTION, CONTEXT, APPLIED_ON,
                MIGRATION_TABLE_NAME,
                ID, ID, ID,
                ID);
    }

    @Override
    public String getSelectLastMigrationsByContext() {
        return String.format(
                "WITH break_row (%s) AS (" +
                        "  SELECT TOP 1 %s FROM %s WHERE %s IS NULL OR %s != ? ORDER BY %s DESC) " +
                        "SELECT mh.%s, mh.%s, mh.%s, mh.%s, mh.%s " +
                        "FROM %s mh " +
                        "LEFT JOIN break_row ON (1 = 1) " +
                        "WHERE mh.%s = ? AND (break_row.%s IS NULL OR mh.%s > break_row.%s) " +
                        "ORDER BY mh.%s DESC",
                ID,
                ID, MIGRATION_TABLE_NAME, CONTEXT, CONTEXT, ID,
                ID, VERSION, DESCRIPTION, CONTEXT, APPLIED_ON,
                MIGRATION_TABLE_NAME,
                CONTEXT, ID, ID, ID,
                ID);
    }

    @Override
    public String getSelectLastMigrationsByCount() {
        return String.format(
                "SELECT " +
                        "  r.%s, r.%s, r.%s, r.%s, r.%s " +
                        "FROM ( " +
                        "  SELECT " +
                        "    ROW_NUMBER() OVER (ORDER BY mh.%s DESC) AS rn, " +
                        "    mh.%s, mh.%s, mh.%s, mh.%s, mh.%s " +
                        "  FROM %s mh " +
                        ") AS r " +
                        "WHERE r.rn <= ? " +
                        "ORDER BY r.rn",
                ID, VERSION, DESCRIPTION, CONTEXT, APPLIED_ON,
                ID,
                ID, VERSION, DESCRIPTION, CONTEXT, APPLIED_ON,
                MIGRATION_TABLE_NAME);
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

    @Override
    public String getLockForExecutionSql() {
        return String.format(
                "SELECT TOP 0 NULL FROM %s WITH (TABLOCKX, HOLDLOCK)",
                MIGRATION_TABLE_NAME);
    }

    @Override
    public String getAcquireAdvisoryLockSql() {
        return String.format(
                "EXEC sp_getapplock @Resource = '%s', @LockMode = 'Exclusive', @LockTimeout = -1, @LockOwner = '%s'",
                ADVISORY_LOCK_NAME,
                ADVISORY_LOCK_OWNER);
    }

    @Override
    public String getReleaseAdvisoryLockSql() {
        return String.format(
                "EXEC sp_releaseapplock @Resource = '%s', @LockOwner = '%s'",
                ADVISORY_LOCK_NAME,
                ADVISORY_LOCK_OWNER);
    }
}
