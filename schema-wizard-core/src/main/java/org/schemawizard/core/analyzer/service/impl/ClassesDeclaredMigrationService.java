package org.schemawizard.core.analyzer.service.impl;

import org.schemawizard.core.analyzer.DeclaredMigration;
import org.schemawizard.core.analyzer.service.DeclaredMigrationService;
import org.schemawizard.core.analyzer.exception.MigrationAnalyzerException;
import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.annotation.SWName;
import org.schemawizard.core.model.ConfigurationProperties;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.reflections.scanners.Scanners.SubTypes;

public class ClassesDeclaredMigrationService implements DeclaredMigrationService {

    private final ConfigurationProperties configurationProperties;
    private static final Pattern MIGRATION_CLASS_NAME_PATTERN = Pattern.compile("^SW(\\d+)(\\D+\\w*)");

    public ClassesDeclaredMigrationService(ConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    @Override
    public List<DeclaredMigration> getDeclaredMigrations() {
        Reflections reflections = new Reflections(configurationProperties.getMigrationsPackage());
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
                    String.format(
                            "Class name '%s' doesn't match the pattern '%s'",
                            className,
                            "SW<version><description>"));
        }

        int version = Integer.parseInt(matcher.group(1));
        String description = Optional.ofNullable(migrationClass.getAnnotation(SWName.class))
                .map(SWName::value)
                .orElse(descriptionFromClassName(matcher.group(2)));
        @SuppressWarnings("unchecked")
        var dbMigrationClass = (Class<? extends Migration>) migrationClass;

        return new DeclaredMigration(
                version,
                description,
                dbMigrationClass
        );
    }

    private String descriptionFromClassName(String descriptionClassNamePart) {
        return Arrays.stream(descriptionClassNamePart.split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])"))
                .map(String::toLowerCase)
                .collect(Collectors.joining(" "));
    }
}
