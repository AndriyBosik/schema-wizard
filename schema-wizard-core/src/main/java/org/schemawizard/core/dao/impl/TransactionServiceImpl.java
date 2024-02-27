package org.schemawizard.core.dao.impl;

import org.schemawizard.core.dao.ConnectionHolder;
import org.schemawizard.core.dao.TransactionService;
import org.schemawizard.core.exception.MigrationApplicationException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class TransactionServiceImpl implements TransactionService {
    private final ConnectionHolder connectionHolder;
    private final ThreadLocal<Integer> openedTransactions = ThreadLocal.withInitial(() -> 0);

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

    @Override
    public void doWithinTransaction(Runnable action) {
        try {
            apply(connection -> {
                action.run();
                return null;
            });
        } catch (SQLException exception) {
            throw new MigrationApplicationException(exception.getMessage(), exception);
        }
    }

    private <T> T apply(Function<Connection, T> action) throws SQLException {
        Connection connection = connectionHolder.getConnection();
        openedTransactions.set(openedTransactions.get() + 1);
        try {
            connection.setAutoCommit(false);
            T result = action.apply(connection);
            openedTransactions.set(openedTransactions.get() - 1);
            if (openedTransactions.get() == 0) {
                connection.commit();
            }
            return result;
        } catch (Exception exception) {
            openedTransactions.set(0);
            connection.rollback();
            throw new MigrationApplicationException(exception.getMessage(), exception);
        } finally {
            if (openedTransactions.get() == 0) {
                connection.close();
            }
        }
    }
}
