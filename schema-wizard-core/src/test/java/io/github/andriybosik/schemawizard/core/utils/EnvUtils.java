package io.github.andriybosik.schemawizard.core.utils;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;

public class EnvUtils {
    /* COMMON */
    public static final String DB_DATABASE = System.getenv("DB_DATABASE");
    public static final String DB_USERNAME = System.getenv("DB_USERNAME");
    public static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    public static final String DB_URL = System.getenv("DB_URL");
    public static final String DB_SCHEMA = System.getenv("DB_SCHEMA");
    public static final int DB_PORT = Integer.parseInt(System.getenv("DB_PORT"));
    public static final DatabaseProvider PROVIDER = DatabaseProvider.valueOf(System.getenv("PROVIDER"));

    /* ORACLE */
    public static final String ORACLE_DB_APP_USERNAME = System.getenv("DB_APP_USERNAME");
}
