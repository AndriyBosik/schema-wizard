package io.github.andriybosik.schemawizard.core.analyzer.service;

import io.github.andriybosik.schemawizard.core.analyzer.DeclaredMigration;

import java.util.List;

public interface DeclaredMigrationService {
    List<DeclaredMigration> getDeclaredMigrations();
}
