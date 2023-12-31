package org.schemawizard.core.analyzer;

import java.util.List;

public interface MigrationAnalyzer {
    List<MigrationData> upgradeAnalyze();

    List<MigrationData> downgradeAnalyze(int version);
}
