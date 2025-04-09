package io.github.andriybosik.schemawizard.core.dao;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;

import java.sql.Driver;

public interface DriverLoader {
    Driver load(DatabaseProvider databaseProvider);
}
