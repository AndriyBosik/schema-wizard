package io.github.andriybosik.schemawizard.core.model;

import io.github.andriybosik.schemawizard.core.metadata.ColumnNamingStrategy;
import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;
import io.github.andriybosik.schemawizard.core.model.defaults.Defaults;

public class ConfigurationProperties {
    private final DatabaseProvider databaseProvider;
    private final String context;
    private final String connectionUrl;
    private final String username;
    private final String password;
    private final String migrationsPackage;
    private final String extensionPackage;
    private final boolean logGeneratedSql;
    private final ColumnNamingStrategy columnNamingStrategy;

    private final Defaults defaults;

    public ConfigurationProperties(
            DatabaseProvider databaseProvider,
            String context,
            String connectionUrl,
            String username,
            String password,
            String migrationsPackage,
            String extensionPackage,
            boolean logGeneratedSql,
            ColumnNamingStrategy columnNamingStrategy,
            Defaults defaults
    ) {
        this.databaseProvider = databaseProvider;
        this.context = context;
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.password = password;
        this.migrationsPackage = migrationsPackage;
        this.extensionPackage = extensionPackage;
        this.logGeneratedSql = logGeneratedSql;
        this.columnNamingStrategy = columnNamingStrategy;
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

    public String getExtensionPackage() {
        return extensionPackage;
    }

    public boolean isLogGeneratedSql() {
        return logGeneratedSql;
    }

    public ColumnNamingStrategy getColumnNamingStrategy() {
        return columnNamingStrategy;
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
        private String extensionPackage;
        private boolean logGeneratedSql;
        private ColumnNamingStrategy columnNamingStrategy;

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

        public PropertyConfigurationBuilder extensionPackage(String extensionPackage) {
            this.extensionPackage = extensionPackage;
            return this;
        }

        public PropertyConfigurationBuilder logGeneratedSql(boolean logGeneratedSql) {
            this.logGeneratedSql = logGeneratedSql;
            return this;
        }

        public PropertyConfigurationBuilder columnNamingStrategy(ColumnNamingStrategy columnNamingStrategy) {
            this.columnNamingStrategy = columnNamingStrategy;
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
                    extensionPackage,
                    logGeneratedSql,
                    columnNamingStrategy,
                    defaults);
        }
    }
}
