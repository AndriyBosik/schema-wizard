package org.schemawizard.core.analyzer;

import org.schemawizard.core.migration.Migration;

public class DeclaredMigration {
    private final int version;
    private final String description;
    private final Class<? extends Migration> migrationClass;

    public DeclaredMigration(int version, String description, Class<? extends Migration> migrationClass) {
        this.version = version;
        this.description = description;
        this.migrationClass = migrationClass;
    }

    public int getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public Class<? extends Migration> getMigrationClass() {
        return migrationClass;
    }
}
