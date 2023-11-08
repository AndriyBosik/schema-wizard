package com.example.dao;

public interface HistoryTableQueryFactory {
    String getCreateMigrationHistoryTableSql();
    String getSelectTableSql();
    String getSelectMigrationsSql();
}
