package io.github.andriybosik.schemawizard.core.migration.metadata;

public class ColumnTypeFactoryQualifier {
    public static final String POSTGRESQL = "postgresqlColumnTypeFactory";
    public static final String ORACLE = "oracleColumnTypeFactory";
    public static final String MYSQL = "mysqlColumnTypeFactory";
    public static final String SQLSERVER = "sqlserverColumnTypeFactory";

    private ColumnTypeFactoryQualifier() {
    }
}
