package io.github.andriybosik.schemawizard.core.analyzer;

import io.github.andriybosik.schemawizard.core.analyzer.model.DowngradeStrategyParameters;

import java.util.List;

public interface MigrationAnalyzer {
    List<MigrationData> upgradeAnalyze();

    List<MigrationData> downgradeAnalyze(DowngradeStrategyParameters parameters);
}
