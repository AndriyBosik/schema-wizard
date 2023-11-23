package com.example.migration.model;

public class MigrationInfo {
    private final String sql;

    public MigrationInfo() {
        this.sql = null;
    }

    public MigrationInfo(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
