package com.example.analyzer;

import com.example.migration.Migration;

public class DeclaredMigration implements Comparable<DeclaredMigration> {
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

    @Override
    public int compareTo(DeclaredMigration declaredMigration) {
        return Integer.compare(version, declaredMigration.getVersion());
    }
}