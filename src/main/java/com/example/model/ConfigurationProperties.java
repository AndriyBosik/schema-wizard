package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationProperties {
    private final String connectionUrl;
    private final String username;
    private final String password;
    private final String migrationsPackage;
    private final List<LoggingItem> logging;

    public ConfigurationProperties(
            String connectionUrl,
            String username,
            String password,
            String migrationsPackage,
            List<LoggingItem> logging
    ) {
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.password = password;
        this.migrationsPackage = migrationsPackage;
        this.logging = logging;
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

    public List<LoggingItem> getLogging() {
        return logging;
    }

    public static PropertyConfigurationBuilder builder() {
        return new PropertyConfigurationBuilder();
    }

    public static class LoggingItem {
        private final String item;
        private final String level;
        private final boolean enabled;

        public LoggingItem(String item, String level, boolean enabled) {
            this.item = item;
            this.level = level;
            this.enabled = enabled;
        }

        public String getItem() {
            return item;
        }

        public String getLevel() {
            return level;
        }

        public boolean isEnabled() {
            return enabled;
        }
    }

    public static class PropertyConfigurationBuilder {
        private String connectionUrl;
        private String username;
        private String password;
        private String migrationsPackage;
        private List<LoggingItem> logging = new ArrayList<>();

        private PropertyConfigurationBuilder() {
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

        public PropertyConfigurationBuilder logging(List<LoggingItem> logging) {
            this.logging = logging;
            return this;
        }

        public ConfigurationProperties build() {
            return new ConfigurationProperties(connectionUrl, username, password, migrationsPackage, logging);
        }
    }
}
