package org.schemawizard.core.service.impl;

import org.schemawizard.core.dao.ConnectionHolder;
import org.schemawizard.core.exception.MigrationApplicationException;
import org.schemawizard.core.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class TransactionServiceImpl implements TransactionService {
    private final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final ConnectionHolder connectionHolder;

    public TransactionServiceImpl(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    @Override
    public <T> T doWithinTransaction(Function<Connection, T> action) {
        Connection connection = connectionHolder.getConnection();
        try {
            connection.setAutoCommit(false);
            T result = action.apply(connection);
            connection.commit();
            return result;
        } catch (Exception exception) {
            try {
                connection.rollback();
            } catch (SQLException sqlException) {
                throw new MigrationApplicationException(sqlException.getMessage(), sqlException);
            }
            throw new MigrationApplicationException(exception.getMessage(), exception);
        } finally {
            try {
                connection.close();
            } catch (SQLException sqlException) {
                log.warn("Unable to close database connection", sqlException);
            }
        }
    }
}
