package org.schemawizard.core.analyzer.service;

import org.schemawizard.core.analyzer.DeclaredMigration;

import java.util.List;

public interface DeclaredMigrationService {
    List<DeclaredMigration> getDeclaredMigrations();
}
