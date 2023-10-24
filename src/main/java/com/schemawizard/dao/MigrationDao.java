package com.schemawizard.dao;

import com.schemawizard.dao.entity.MigrationEntity;

import java.util.List;

public interface MigrationDao {
    List<MigrationEntity> getAllMigrations();
}
