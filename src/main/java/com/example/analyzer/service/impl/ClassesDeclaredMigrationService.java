package com.example.analyzer.service.impl;

import com.example.analyzer.DeclaredMigration;
import com.example.analyzer.service.DeclaredMigrationService;
import com.example.analyzer.exception.MigrationAnalyzerException;
import com.example.migration.Migration;
import org.reflections.Reflections;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.reflections.scanners.Scanners.SubTypes;

public class ClassesDeclaredMigrationService implements DeclaredMigrationService {

    private final String packageName;

    private static final Pattern MIGRATION_CLASS_NAME_PATTERN = Pattern.compile("^SW(\\d+)(\\D+\\w*)");

    public ClassesDeclaredMigrationService(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public List<DeclaredMigration> getDeclaredMigrations() {

        Reflections reflections = new Reflections(packageName);
        var migrationClasses = reflections.get(SubTypes.of(Migration.class).asClass());
        return migrationClasses.stream()
                .map(this::migrationClassToDeclaredMigration)
                .sorted(Comparator.comparing(DeclaredMigration::getVersion))
                .collect(Collectors.toList());
    }

    private DeclaredMigration migrationClassToDeclaredMigration(Class<?> migrationClass) {
        String className = migrationClass.getSimpleName();
        var matcher = MIGRATION_CLASS_NAME_PATTERN.matcher(className);
        if (!matcher.matches()) {
            throw new MigrationAnalyzerException(
                    String.format("Class name '%s' doesn't match the pattern '%s'",
                            className, MIGRATION_CLASS_NAME_PATTERN.pattern()));
        }

        int version = Integer.parseInt(matcher.group(1));
        String description = matcher.group(2);
        @SuppressWarnings("unchecked")
        var dbMigrationClass = (Class<? extends Migration>) migrationClass;

        return new DeclaredMigration(
                version,
                description,
                dbMigrationClass
        );
    }
}
