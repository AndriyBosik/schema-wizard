package org.schemawizard.core.analyzer;

import org.schemawizard.core.migration.Migration;

public class MigrationData {
    private final int version;
    private final String description;
    private final Migration migration;

    public MigrationData(int version, String description, Migration migration) {
        this.version = version;
        this.description = description;
        this.migration = migration;
    }

    public int getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public Migration getMigration() {
        return migration;
    }
}
