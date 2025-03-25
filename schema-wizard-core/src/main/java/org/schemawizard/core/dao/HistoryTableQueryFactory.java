package org.schemawizard.core.dao;

public interface HistoryTableQueryFactory {
    String getCreateMigrationHistoryTableSql();

    String getSelectTableSql();

    String getSelectMigrationsSql();

    String getSelectMigrationsStartedFromSqlOrderByIdDesc();

    String getSelectLastMigrationsByContext();

    String getSelectLastMigrationsByCount();

    String getInsertMigrationHistoryRowQuery();

    String getDeleteMigrationHistoryRowQuery();

    String getLockForExecutionSql();

    String getAcquireAdvisoryLockSql();

    String getReleaseAdvisoryLockSql();
}
