package org.schemawizard.core.dao;

public interface HistoryTableQueryFactory {
    String getCreateMigrationHistoryTableSql();

    String getSelectTableSql();

    String getSelectMigrationsSql();

    String getSelectMigrationsStartedFromSqlOrderByIdDesc();

    String getSelectLastMigrationsByContext();

    String getInsertMigrationHistoryRowQuery();

    String getDeleteMigrationHistoryRowQuery();

    String getLockForMigrationExecutionSql();

    String getAcquireAdvisoryLockSql();

    String getReleaseAdvisoryLockSql();
}
