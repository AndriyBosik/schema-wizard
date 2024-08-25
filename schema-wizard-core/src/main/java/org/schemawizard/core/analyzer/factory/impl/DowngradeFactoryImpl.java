package org.schemawizard.core.analyzer.factory.impl;

import org.schemawizard.core.analyzer.exception.MigrationAnalyzerException;
import org.schemawizard.core.analyzer.exception.UnknownDowngradeParametersException;
import org.schemawizard.core.analyzer.factory.DowngradeFactory;
import org.schemawizard.core.analyzer.model.ContextDowngradeStrategyParameters;
import org.schemawizard.core.analyzer.model.CountDowngradeStrategyParameters;
import org.schemawizard.core.analyzer.model.DowngradeStrategyParameters;
import org.schemawizard.core.analyzer.model.VersionDowngradeStrategyParameters;
import org.schemawizard.core.analyzer.service.DowngradeStrategy;
import org.schemawizard.core.dao.HistoryTableQueryFactory;
import org.schemawizard.core.dao.TransactionService;
import org.schemawizard.core.metadata.ErrorMessage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Function;

public class DowngradeFactoryImpl implements DowngradeFactory {
    private final TransactionService transactionService;
    private final HistoryTableQueryFactory historyTableQueryFactory;

    public DowngradeFactoryImpl(
            TransactionService transactionService,
            HistoryTableQueryFactory historyTableQueryFactory
    ) {
        this.transactionService = transactionService;
        this.historyTableQueryFactory = historyTableQueryFactory;
    }

    @Override
    public DowngradeStrategy getInstance(DowngradeStrategyParameters parameters) {
        if (parameters instanceof VersionDowngradeStrategyParameters) {
            return versionStrategy((VersionDowngradeStrategyParameters) parameters);
        } else if (parameters instanceof ContextDowngradeStrategyParameters) {
            return contextStrategy((ContextDowngradeStrategyParameters) parameters);
        } else if (parameters instanceof CountDowngradeStrategyParameters) {
            return countStrategy((CountDowngradeStrategyParameters) parameters);
        }
        throw new UnknownDowngradeParametersException(
                String.format(
                        ErrorMessage.UNABLE_TO_RESOLVE_DOWNGRADE_PARAMETERS_CLASS_TEMPLATE,
                        parameters.getClass()));
    }

    private DowngradeStrategy versionStrategy(VersionDowngradeStrategyParameters parameters) {
        return new DowngradeStrategy() {
            @Override
            public <T> T apply(Function<PreparedStatement, T> action) {
                return transactionService.doWithinTransaction(connection -> {
                    try (PreparedStatement statement = connection.prepareStatement(historyTableQueryFactory.getSelectMigrationsStartedFromSqlOrderByIdDesc())) {
                        statement.setInt(1, parameters.getVersion());
                        return action.apply(statement);
                    } catch (SQLException exception) {
                        throw new MigrationAnalyzerException(exception.getMessage(), exception);
                    }
                });
            }

            @Override
            public String getNoMigrationsFoundMessage() {
                return String.format(ErrorMessage.MIGRATION_WITH_VERSION_WAS_NOT_FOUND_TEMPLATE, parameters.getVersion());
            }
        };
    }

    private DowngradeStrategy contextStrategy(ContextDowngradeStrategyParameters parameters) {
        return new DowngradeStrategy() {
            @Override
            public <T> T apply(Function<PreparedStatement, T> action) {
                return transactionService.doWithinTransaction(connection -> {
                    try (PreparedStatement statement = connection.prepareStatement(historyTableQueryFactory.getSelectLastMigrationsByContext())) {
                        statement.setString(1, parameters.getContext());
                        statement.setString(2, parameters.getContext());
                        return action.apply(statement);
                    } catch (SQLException exception) {
                        throw new MigrationAnalyzerException(exception.getMessage(), exception);
                    }
                });
            }

            @Override
            public String getNoMigrationsFoundMessage() {
                return String.format(ErrorMessage.DOWNGRADE_CONTEXT_IS_INVALID_TEMPLATE, parameters.getContext());
            }
        };
    }

    private DowngradeStrategy countStrategy(CountDowngradeStrategyParameters parameters) {
        return new DowngradeStrategy() {
            @Override
            public <T> T apply(Function<PreparedStatement, T> action) {
                try (
                        Connection connection = connectionHolder.getConnection();
                        PreparedStatement statement = connection.prepareStatement(historyTableQueryFactory.getSelectLastMigrationsByCount())
                ) {
                    statement.setInt(1, parameters.getCount());
                    return action.apply(statement);
                } catch (SQLException exception) {
                    throw new MigrationAnalyzerException(exception.getMessage(), exception);
                }
            }

            @Override
            public String getNoMigrationsFoundMessage() {
                return ErrorMessage.MIGRATIONS_WERE_NOT_FOUND;
            }
        };
    }
}
