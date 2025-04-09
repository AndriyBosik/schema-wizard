package io.github.andriybosik.schemawizard.core.runner;

import io.github.andriybosik.schemawizard.core.analyzer.MigrationData;

import java.util.List;

public interface MigrationRunner {
    void upgrade(List<MigrationData> upgradeMigrations);

    void downgrade(List<MigrationData> downgradeMigrations);
}
