package org.schemawizard.core.model;

import org.schemawizard.core.metadata.DatabaseProvider;

public class ConfigurationProperties {
    private final DatabaseProvider databaseProvider;
    private final String connectionUrl;
    private final String username;
    private final String password;
    private final String migrationsPackage;
    private final boolean logGeneratedSql;

    public ConfigurationProperties(
            DatabaseProvider databaseProvider,
            String connectionUrl,
            String username,
            String password,
            String migrationsPackage,
            boolean logGeneratedSql
    ) {
        this.databaseProvider = databaseProvider;
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.password = password;
        this.migrationsPackage = migrationsPackage;
        this.logGeneratedSql = logGeneratedSql;
    }

    public DatabaseProvider getDatabaseProvider() {
        return databaseProvider;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMigrationsPackage() {
        return migrationsPackage;
    }

    public boolean isLogGeneratedSql() {
        return logGeneratedSql;
    }

    public static PropertyConfigurationBuilder builder() {
        return new PropertyConfigurationBuilder();
    }

    public static class PropertyConfigurationBuilder {
        private DatabaseProvider databaseProvider;
        private String connectionUrl;
        private String username;
        private String password;
        private String migrationsPackage;
        private boolean logGeneratedSql;

        private PropertyConfigurationBuilder() {
        }

        public PropertyConfigurationBuilder databaseProvider(DatabaseProvider databaseProvider) {
            this.databaseProvider = databaseProvider;
            return this;
        }

        public PropertyConfigurationBuilder connectionUrl(String connectionUrl) {
            this.connectionUrl = connectionUrl;
            return this;
        }

        public PropertyConfigurationBuilder username(String username) {
            this.username = username;
            return this;
        }

        public PropertyConfigurationBuilder password(String password) {
            this.password = password;
            return this;
        }

        public PropertyConfigurationBuilder migrationsPackage(String migrationsPackage) {
            this.migrationsPackage = migrationsPackage;
            return this;
        }

        public PropertyConfigurationBuilder logGeneratedSql(boolean logGeneratedSql) {
            this.logGeneratedSql = logGeneratedSql;
            return this;
        }

        public ConfigurationProperties build() {
            return new ConfigurationProperties(
                    databaseProvider,
                    connectionUrl,
                    username,
                    password,
                    migrationsPackage,
                    logGeneratedSql);
        }
    }
}
