package org.schemawizard.core.runner.impl;

import org.schemawizard.core.analyzer.MigrationData;
import org.schemawizard.core.analyzer.impl.HistoryTableCreatorImpl;
import org.schemawizard.core.callback.AfterQueryExecutionCallback;
import org.schemawizard.core.callback.BeforeQueryExecutionCallback;
import org.schemawizard.core.dao.HistoryTableQueryFactory;
import org.schemawizard.core.dao.TransactionService;
import org.schemawizard.core.exception.MigrationApplicationException;
import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.CompositeOperation;
import org.schemawizard.core.migration.operation.Operation;
import org.schemawizard.core.migration.service.OperationResolverService;
import org.schemawizard.core.runner.MigrationRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class MigrationRunnerImpl implements MigrationRunner {
    private final Logger log = LoggerFactory.getLogger(HistoryTableCreatorImpl.class);

    private final OperationResolverService operationResolverService;
    private final HistoryTableQueryFactory historyTableQueryFactory;
    private final TransactionService transactionService;
    private final List<BeforeQueryExecutionCallback> beforeQueryCallbacks;
    private final List<AfterQueryExecutionCallback> afterQueryCallbacks;

    public MigrationRunnerImpl(
            OperationResolverService operationResolverService,
            HistoryTableQueryFactory historyTableQueryFactory,
            TransactionService transactionService,
            List<BeforeQueryExecutionCallback> beforeQueryCallbacks,
            List<AfterQueryExecutionCallback> afterQueryCallbacks
    ) {
        this.operationResolverService = operationResolverService;
        this.historyTableQueryFactory = historyTableQueryFactory;
        this.transactionService = transactionService;
        this.beforeQueryCallbacks = beforeQueryCallbacks;
        this.afterQueryCallbacks = afterQueryCallbacks;
    }

    @Override
    public void upgrade(List<MigrationData> upgradeMigrations) {
        MigrationContext context = new MigrationContext();
        upgrade(upgradeMigrations, context);
    }

    private List<Operation> getOperations(MigrationData data, MigrationContext context) {
        Migration migration = data.getMigration();
        Operation operation = migration.up(context);
        return unfold(operation);
    }

    public List<Operation> unfold(Operation operation) {
        if (operation instanceof CompositeOperation) {
            CompositeOperation compositeOperation = (CompositeOperation) operation;
            return compositeOperation.getOperations().stream()
                    .map(this::unfold)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        }
        return List.of(operation);
    }

    private void upgrade(List<MigrationData> data, MigrationContext context) {
        transactionService.doWithinTransaction(connection -> {
            for (MigrationData item : data) {
                apply(connection, item, context);
            }
            return null;
        });
    }

    private void apply(Connection connection, MigrationData data, MigrationContext context) {
        List<Operation> operations = getOperations(data, context);
        beforeQueryCallbacks.forEach(callback -> callback.handle(data));
        try (
                Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement(historyTableQueryFactory.getInsertMigrationHistoryRowQuery())
        ) {
            operations.stream()
                    .map(operationResolverService::resolve)
                    .map(MigrationInfo::getSql)
                    .forEach(sql -> executeStatement(statement, sql));
            fillPreparedStatement(data, preparedStatement);
            preparedStatement.execute();
        } catch (SQLException exception) {
            throw new MigrationApplicationException(exception.getMessage(), exception);
        }
        afterQueryCallbacks.forEach(callback -> callback.handle(data));
    }

    private void executeStatement(Statement statement, String sql) {
        try {
            statement.execute(sql);
        } catch (SQLException exception) {
            throw new MigrationApplicationException(exception.getMessage(), exception);
        }
    }

    private void fillPreparedStatement(MigrationData item, PreparedStatement statement) {
        try {
            statement.setInt(1, item.getVersion());
            statement.setString(2, item.getDescription());
            statement.addBatch();
        } catch (SQLException exception) {
            throw new MigrationApplicationException(exception.getMessage(), exception);
        }
    }
}
