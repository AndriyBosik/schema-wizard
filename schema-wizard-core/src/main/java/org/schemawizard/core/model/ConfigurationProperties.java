package org.schemawizard.core.model;

import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.model.defaults.Defaults;

public class ConfigurationProperties {
    private final DatabaseProvider databaseProvider;
    private final String context;
    private final String connectionUrl;
    private final String username;
    private final String password;
    private final String migrationsPackage;
    private final boolean logGeneratedSql;

    private final Defaults defaults;

    public ConfigurationProperties(
            DatabaseProvider databaseProvider,
            String context,
            String connectionUrl,
            String username,
            String password,
            String migrationsPackage,
            boolean logGeneratedSql,
            Defaults defaults
    ) {
        this.databaseProvider = databaseProvider;
        this.context = context;
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.password = password;
        this.migrationsPackage = migrationsPackage;
        this.logGeneratedSql = logGeneratedSql;
        this.defaults = defaults;
    }

    public Defaults getDefaults() {
        return defaults;
    }

    public DatabaseProvider getDatabaseProvider() {
        return databaseProvider;
    }

    public String getContext() {
        return context;
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
        private String context;
        private String connectionUrl;
        private String username;
        private String password;
        private String migrationsPackage;
        private boolean logGeneratedSql;

        private Defaults defaults;

        private PropertyConfigurationBuilder() {
        }
        public PropertyConfigurationBuilder defaults(Defaults defaults) {
            this.defaults = defaults;
            return this;
        }

        public PropertyConfigurationBuilder databaseProvider(DatabaseProvider databaseProvider) {
            this.databaseProvider = databaseProvider;
            return this;
        }

        public PropertyConfigurationBuilder context(String context) {
            this.context = context;
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
                    context,
                    connectionUrl,
                    username,
                    password,
                    migrationsPackage,
                    logGeneratedSql,
                    defaults);
        }
    }
}
