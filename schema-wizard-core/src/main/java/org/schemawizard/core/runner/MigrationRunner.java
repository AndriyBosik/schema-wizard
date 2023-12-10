package org.schemawizard.core.runner;

import org.schemawizard.core.analyzer.MigrationData;

import java.util.List;

public interface MigrationRunner {
    void upgrade(List<MigrationData> upgradeMigrations);
}
