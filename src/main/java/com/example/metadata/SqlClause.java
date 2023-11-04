package com.example.metadata;

public class SqlClause {
    public static final String COLUMNS_SEPARATOR = ", ";

    public static final String CREATE_TABLE = "CREATE TABLE";
    public static final String ALTER_TABLE = "ALTER TABLE";
    public static final String DROP_TABLE = "DROP TABLE";
    public static final String ADD_COLUMN = "ADD COLUMN";
    public static final String DROP_COLUMN = "DROP COLUMN";
    public static final String DROP = "DROP";
    public static final String PRIMARY_KEY = "PRIMARY KEY";
    public static final String FOREIGN_KEY = "FOREIGN KEY";
    public static final String UNIQUE = "UNIQUE";
    public static final String CONSTRAINT = "CONSTRAINT";
    public static final String REFERENCES = "REFERENCES";
    public static final String IF_EXISTS = "IF EXISTS";
    public static final String IF_NOT_EXISTS = "IF NOT EXISTS";
    public static final String ADD_CONSTRAINT = "ADD CONSTRAINT";
    public static final String DROP_CONSTRAINT = "DROP CONSTRAINT";

    private SqlClause() {
    }
}
