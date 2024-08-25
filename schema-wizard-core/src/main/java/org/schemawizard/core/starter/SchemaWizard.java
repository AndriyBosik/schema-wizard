package org.schemawizard.core.starter;

import org.schemawizard.core.analyzer.HistoryTable;
import org.schemawizard.core.analyzer.MigrationAnalyzer;
import org.schemawizard.core.analyzer.MigrationData;
import org.schemawizard.core.analyzer.model.ContextDowngradeStrategyParameters;
import org.schemawizard.core.analyzer.model.CountDowngradeStrategyParameters;
import org.schemawizard.core.analyzer.model.VersionDowngradeStrategyParameters;
import org.schemawizard.core.dao.TransactionService;
import org.schemawizard.core.runner.MigrationRunner;

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
        transactionService.doWithinTransaction(() -> {
            List<MigrationData> downgradeMigrations = migrationAnalyzer.downgradeAnalyze(new ContextDowngradeStrategyParameters(context));
            migrationRunner.downgrade(downgradeMigrations);
        });
    }

    public void downByCount(int count) {
        transactionService.doWithinTransaction(() -> {
            List<MigrationData> downgradeMigrations = migrationAnalyzer.downgradeAnalyze(new CountDowngradeStrategyParameters(count));
            migrationRunner.downgrade(downgradeMigrations);
        });
    }

    private void doWithinHistoryTable(Runnable runnable) {
        transactionService.doWithinTransaction(historyTable::createIfNotExists);
        transactionService.doWithinTransaction(runnable);
    }
}
