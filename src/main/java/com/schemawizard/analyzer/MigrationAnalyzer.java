package com.schemawizard.analyzer;

import java.util.List;

public interface MigrationAnalyzer {
    List<Migration> analyze();
}
