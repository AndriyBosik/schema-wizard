package org.schemawizard.core.dao.impl;

import org.schemawizard.core.dao.HistoryTableQueryFactory;

import static org.schemawizard.core.dao.Constants.APPLIED_ON;
import static org.schemawizard.core.dao.Constants.CONTEXT;
import static org.schemawizard.core.dao.Constants.DESCRIPTION;
import static org.schemawizard.core.dao.Constants.ID;
import static org.schemawizard.core.dao.Constants.MIGRATION_TABLE_NAME;
import static org.schemawizard.core.dao.Constants.VERSION;

public class OracleHistoryTableQueryFactory implements HistoryTableQueryFactory {
    private final static String METADATA_TABLE_NAME = "USER_TABLES";
    private final static String MIGRATION_HISTORY_TABLE_CREATION_LOCK_NAME = "SCHEMAWIZARD_LOCK";
    private final static String EXECUTION_LOCK_NAME = "SCHEMAWIZARD_EXECUTION_LOCK";

    @Override
    public String getCreateMigrationHistoryTableSql() {
        return String.format("CREATE TABLE %s ("
                        + "%s NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY PRIMARY KEY, "
                        + "%s INTEGER NOT NULL, "
                        + "%s VARCHAR2(255) NOT NULL, "
                        + "%s VARCHAR2(255), "
                        + "%s TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,"
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
                        + "WHERE LOWER(table_name)='%s'",
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
        return String.format("WITH break_row AS (" +
                        "SELECT id FROM %s WHERE %s = ?) " +
                        "SELECT mh.%s, mh.%s, mh.%s, mh.%s, mh.%s " +
                        "FROM %s mh " +
                        "LEFT JOIN break_row ON (1 = 1) " +
                        "WHERE break_row.%s IS NOT NULL AND mh.%s >= break_row.%s " +
                        "ORDER BY %s DESC",
                MIGRATION_TABLE_NAME, VERSION,
                ID, VERSION, DESCRIPTION, CONTEXT, APPLIED_ON,
                MIGRATION_TABLE_NAME,
                ID, ID, ID,
                ID);
    }

    @Override
    public String getSelectLastMigrationsByContext() {
        return String.format("WITH break_row AS (" +
                        "  SELECT * FROM (" +
                        "    SELECT * FROM %s WHERE %s IS NULL OR %s != ? ORDER BY %s DESC)" +
                        "  WHERE ROWNUM <= 1) " +
                        "SELECT mh.%s, mh.%s, mh.%s, mh.%s, mh.%s " +
                        "FROM %s mh " +
                        "LEFT JOIN break_row ON (1 = 1) " +
                        "WHERE mh.%s = ? AND (break_row.%s IS NULL OR mh.%s > break_row.%s) " +
                        "ORDER BY mh.%s DESC",
                MIGRATION_TABLE_NAME, CONTEXT, CONTEXT, ID,
                ID, VERSION, DESCRIPTION, CONTEXT, APPLIED_ON,
                MIGRATION_TABLE_NAME,
                CONTEXT, ID, ID, ID,
                ID);
    }

    @Override
    public String getSelectLastMigrationsByCount() {
        return String.format(
                "SELECT mh.%s, mh.%s, mh.%s, mh.%s, mh.%s " +
                        "FROM %s mh " +
                        "ORDER BY mh.%s DESC " +
                        "FETCH FIRST ? ROWS ONLY",
                ID, VERSION, DESCRIPTION, CONTEXT, APPLIED_ON,
                MIGRATION_TABLE_NAME,
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

    @Override
    public String getLockForExecutionSql() {
        return String.format(
                "DECLARE\n" +
                        "  l_lock_handle VARCHAR2(128);\n" +
                        "  l_result NUMBER;\n" +
                        "BEGIN\n" +
                        "  DBMS_LOCK.ALLOCATE_UNIQUE('%s', l_lock_handle);\n" +
                        "  l_result := DBMS_LOCK.REQUEST(l_lock_handle, DBMS_LOCK.X_MODE);\n" +
                        "END;",
                EXECUTION_LOCK_NAME);
    }

    @Override
    public String getAcquireAdvisoryLockSql() {
        return String.format(
                "DECLARE\n" +
                        "  l_lock_handle VARCHAR2(128);\n" +
                        "  l_result NUMBER;\n" +
                        "BEGIN\n" +
                        "  DBMS_LOCK.ALLOCATE_UNIQUE('%s', l_lock_handle);\n" +
                        "  l_result := DBMS_LOCK.REQUEST(l_lock_handle, DBMS_LOCK.X_MODE);\n" +
                        "END;",
                MIGRATION_HISTORY_TABLE_CREATION_LOCK_NAME);
    }

    @Override
    public String getReleaseAdvisoryLockSql() {
        return String.format(
                "DECLARE\n" +
                        "  l_lock_handle VARCHAR2(128);\n" +
                        "  l_result NUMBER;\n" +
                        "BEGIN\n" +
                        "  DBMS_LOCK.ALLOCATE_UNIQUE('%s', l_lock_handle);\n" +
                        "  l_result := DBMS_LOCK.RELEASE(l_lock_handle);\n" +
                        "END;",
                MIGRATION_HISTORY_TABLE_CREATION_LOCK_NAME);
    }
}
