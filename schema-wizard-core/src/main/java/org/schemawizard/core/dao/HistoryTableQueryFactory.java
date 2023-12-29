package org.schemawizard.core.dao;

public interface HistoryTableQueryFactory {
    String getCreateMigrationHistoryTableSql();

    String getSelectTableSql();

    String getSelectMigrationsSql();

    String getSelectMigrationsStartedFromSql();

    String getInsertMigrationHistoryRowQuery();

    String getDeleteMigrationHistoryRowQuery();
}
