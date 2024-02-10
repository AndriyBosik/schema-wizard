package org.schemawizard.core.analyzer;

import org.schemawizard.core.analyzer.model.DowngradeStrategyParameters;

import java.util.List;

public interface MigrationAnalyzer {
    List<MigrationData> upgradeAnalyze();

    List<MigrationData> downgradeAnalyze(DowngradeStrategyParameters parameters);
}
