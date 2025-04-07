package org.schemawizard.core.metadata;

public enum DatabaseProvider {
    POSTGRESQL("org.postgresql.Driver"),
    ORACLE("oracle.jdbc.driver.OracleDriver"),
    MYSQL("com.mysql.jdbc.Driver"),
    MULTI(null);

    private final String driver;

    DatabaseProvider(String driver) {
        this.driver = driver;
    }

    public String getDriver() {
        return driver;
    }
}
