package com.example.dao;

import com.example.dao.exception.DaoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHolder {
    private Connection connection;
    private final String url;
    private final String username;
    private final String password;

    public ConnectionHolder(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, username, password);
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return connection;
    }
}
