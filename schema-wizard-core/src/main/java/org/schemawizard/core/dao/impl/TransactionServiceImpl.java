package org.schemawizard.core.dao.impl;

import org.schemawizard.core.dao.ConnectionHolder;
import org.schemawizard.core.dao.TransactionService;
import org.schemawizard.core.exception.MigrationApplicationException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class TransactionServiceImpl implements TransactionService {
    private final ConnectionHolder connectionHolder;

    public TransactionServiceImpl(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    @Override
    public <T> T doWithinTransaction(Function<Connection, T> action) {
        try {
            return apply(action);
        } catch (SQLException exception) {
            throw new MigrationApplicationException(exception.getMessage(), exception);
        }
    }

    private <T> T apply(Function<Connection, T> action) throws SQLException {
        Connection connection = connectionHolder.getConnection();
        try {
            connection.setAutoCommit(false);
            T result = action.apply(connection);
            connection.commit();
            return result;
        } catch (Exception exception) {
            connection.rollback();
            throw new MigrationApplicationException(exception.getMessage(), exception);
        } finally {
            connection.close();
        }
    }
}
