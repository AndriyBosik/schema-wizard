package com.example.dao;

import com.example.dao.exception.DaoException;
import com.example.model.ConfigurationProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHolder {
    private Connection connection;
    private final ConfigurationProperties configurationProperties;

    public ConnectionHolder(ConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                        configurationProperties.getConnectionUrl(),
                        configurationProperties.getUsername(),
                        configurationProperties.getPassword());
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return connection;
    }
}
