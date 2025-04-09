package io.github.andriybosik.schemawizard.core.dao;

import io.github.andriybosik.schemawizard.core.dao.exception.DaoException;
import io.github.andriybosik.schemawizard.core.model.ConfigurationProperties;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionHolder {
    private Connection connection;
    private final ConfigurationProperties configurationProperties;
    private final Properties properties;
    private final Driver driver;

    public ConnectionHolder(
            ConfigurationProperties configurationProperties,
            DriverLoader driverLoader
    ) {
        this.configurationProperties = configurationProperties;

        this.properties = new Properties();
        this.properties.setProperty("user", configurationProperties.getUsername());
        this.properties.setProperty("password", configurationProperties.getPassword());

        this.driver = driverLoader.load(configurationProperties.getDatabaseProvider());
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = driver.connect(configurationProperties.getConnectionUrl(), properties);
            }
        } catch (SQLException exception) {
            throw new DaoException(exception.getMessage(), exception);
        }
        return connection;
    }
}
