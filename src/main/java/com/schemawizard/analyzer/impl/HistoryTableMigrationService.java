package com.schemawizard.analyzer.impl;

import com.schemawizard.analyzer.Migration;
import com.schemawizard.analyzer.MigrationService;
import com.schemawizard.dao.MigrationDao;
import com.schemawizard.dao.entity.MigrationEntity;

import java.util.List;
import java.util.stream.Collectors;

public class HistoryTableMigrationService implements MigrationService {

    private final MigrationDao migrationDao;

    public HistoryTableMigrationService(MigrationDao migrationDao) {
        this.migrationDao = migrationDao;
    }

    @Override
    public List<Migration> getMigrations() {
        return migrationDao.getAllMigrations()
                .stream()
                .map(this::migrationEntityToMigration)
                .collect(Collectors.toList());
    }

    private Migration migrationEntityToMigration(MigrationEntity entity) {
        Migration migration = new Migration();
        migration.setVersion(entity.getVersion());
        migration.setDescription(entity.getDescription());
        migration.setChecksum(entity.getChecksum());
        migration.setSuccess(entity.isSuccess());
        return migration;
    }
}
