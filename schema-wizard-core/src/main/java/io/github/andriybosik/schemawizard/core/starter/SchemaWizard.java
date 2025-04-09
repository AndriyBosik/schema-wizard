package io.github.andriybosik.schemawizard.core.starter;

import io.github.andriybosik.schemawizard.core.analyzer.HistoryTable;
import io.github.andriybosik.schemawizard.core.analyzer.MigrationAnalyzer;
import io.github.andriybosik.schemawizard.core.analyzer.MigrationData;
import io.github.andriybosik.schemawizard.core.analyzer.exception.MigrationAnalyzerException;
import io.github.andriybosik.schemawizard.core.analyzer.model.ContextDowngradeStrategyParameters;
import io.github.andriybosik.schemawizard.core.analyzer.model.CountDowngradeStrategyParameters;
import io.github.andriybosik.schemawizard.core.analyzer.model.VersionDowngradeStrategyParameters;
import io.github.andriybosik.schemawizard.core.dao.TransactionService;
import io.github.andriybosik.schemawizard.core.metadata.ErrorMessage;
import io.github.andriybosik.schemawizard.core.runner.MigrationRunner;
import io.github.andriybosik.schemawizard.core.utils.StringUtils;

import java.util.List;

public class SchemaWizard {
    private final MigrationRunner migrationRunner;
    private final MigrationAnalyzer migrationAnalyzer;
    private final TransactionService transactionService;
    private final HistoryTable historyTable;

    public SchemaWizard(
            MigrationRunner migrationRunner,
            MigrationAnalyzer migrationAnalyzer,
            TransactionService transactionService,
            HistoryTable historyTable
    ) {
        this.migrationRunner = migrationRunner;
        this.migrationAnalyzer = migrationAnalyzer;
        this.transactionService = transactionService;
        this.historyTable = historyTable;
    }

    public void up() {
        doWithinHistoryTable(() -> {
            List<MigrationData> upgradeMigrations = migrationAnalyzer.upgradeAnalyze();
            migrationRunner.upgrade(upgradeMigrations);
        });
    }

    public void downByVersion(int version) {
        transactionService.doWithinTransaction(() -> {
            List<MigrationData> downgradeMigrations = migrationAnalyzer.downgradeAnalyze(new VersionDowngradeStrategyParameters(version));
            migrationRunner.downgrade(downgradeMigrations);
        });
    }

    public void downByContext(String context) {
        if (StringUtils.isBlank(context)) {
            throw new MigrationAnalyzerException(
                    String.format(
                            ErrorMessage.CONTEXT_VALUE_IS_BLANK_FORMAT, context));
        }
        transactionService.doWithinTransaction(() -> {
            List<MigrationData> downgradeMigrations = migrationAnalyzer.downgradeAnalyze(new ContextDowngradeStrategyParameters(StringUtils.strip(context)));
            migrationRunner.downgrade(downgradeMigrations);
        });
    }

    public void downByCount(int count) {
        if (count == 0) {
            return;
        } else if (count < 0) {
            throw new MigrationAnalyzerException(
                    String.format(
                            ErrorMessage.NEGATIVE_COUNT_TO_DOWNGRADE_FORMAT, count));
        }
        transactionService.doWithinTransaction(() -> {
            List<MigrationData> downgradeMigrations = migrationAnalyzer.downgradeAnalyze(new CountDowngradeStrategyParameters(count));
            if (downgradeMigrations.size() < count) {
                throw new MigrationAnalyzerException(
                        String.format(
                                ErrorMessage.UNABLE_TO_DOWNGRADE_BY_COUNT_FORMAT, downgradeMigrations.size(), count));
            }
            migrationRunner.downgrade(downgradeMigrations);
        });
    }

    private void doWithinHistoryTable(Runnable runnable) {
        transactionService.doWithinTransaction(historyTable::createIfNotExists);
        transactionService.doWithinTransaction(runnable);
    }
}
