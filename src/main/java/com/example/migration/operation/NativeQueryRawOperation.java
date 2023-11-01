package com.example.migration.operation;

public class NativeQueryRawOperation implements Operation {
    private final String sqlQuery;

    public NativeQueryRawOperation(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }
}
