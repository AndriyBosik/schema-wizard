package org.schemawizard.core.analyzer;

import java.util.List;

public interface MigrationAnalyzer {
    List<MigrationData> upgradeAnalyze();

    List<MigrationData> downgradeAnalyze(Integer version);
}
