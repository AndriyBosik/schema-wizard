package com.schemawizard.analyzer.impl;

import com.google.common.reflect.ClassPath;
import com.schemawizard.DbMigration;
import com.schemawizard.analyzer.Migration;
import com.schemawizard.analyzer.MigrationService;
import com.schemawizard.analyzer.exception.MigrationAnalyzerException;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClassesMigrationService implements MigrationService {

    private final String packageName;

    private static final String MIGRATION_CLASS_NAME_PATTERN = "^V(\\d+)__(\\w+)";

    public ClassesMigrationService(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public List<Migration> getMigrations() {

        try {
            var classPath = ClassPath.from(ClassLoader.getSystemClassLoader());
            var topLevelClasses = classPath.getTopLevelClasses(packageName);
            return topLevelClasses.stream()
                    .map(ClassPath.ClassInfo::load)
                    .filter(DbMigration.class::isAssignableFrom)
                    .map(this::convertMigration)
                    .sorted(Comparator.comparing(Migration::getVersion))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Migration convertMigration(Class<?> migrationClass) {
        String className = migrationClass.getSimpleName();
        Pattern pattern = Pattern.compile(MIGRATION_CLASS_NAME_PATTERN);
        var matcher = pattern.matcher(className);
        if (!matcher.matches()) {
            throw new MigrationAnalyzerException("Class name doesn't match the pattern '" + MIGRATION_CLASS_NAME_PATTERN + "'");
        }

        int version = Integer.parseInt(matcher.group(1));
        String description = matcher.group(2);

        Migration migration = new Migration();
        migration.setVersion(version);
        migration.setDescription(description);

        //todo: for now checksum calculation is skipped, version is used instead
        migration.setChecksum(version);

        return migration;
    }
}
