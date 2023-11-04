package com.example.analyzer.service;

import com.example.analyzer.AppliedMigration;

import java.util.List;

public interface AppliedMigrationsService {
    List<AppliedMigration> getAppliedMigrations();
}
