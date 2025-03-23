package org.schemawizard.core.dao;

import org.schemawizard.core.metadata.DatabaseProvider;

import java.sql.Driver;

public interface DriverLoader {
    Driver load(DatabaseProvider databaseProvider);
}
