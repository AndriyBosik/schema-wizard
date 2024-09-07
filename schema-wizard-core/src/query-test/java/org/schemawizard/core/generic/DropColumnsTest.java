package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.migration.builder.operation.DropColumns;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

public class DropColumnsTest extends GenericTest {
    @Test
    public void shouldGenerateWhenSingleColumn() {
        Operation operation = DropColumns.builder("users")
                .columns("first_name")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-columns/single-column", info.getSql());
    }

    @Test
    public void shouldGenerateWhenMultipleColumns() {
        Operation operation = DropColumns.builder("schemawizard", "users")
                .columns("first_name", "enabled")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-columns/multiple-columns", info.getSql());
    }

    @Test
    public void shouldGenerateDropColumnsIfExists() {
        Operation operation = DropColumns.builder("users")
                .columns("first_name")
                .ifExists("age", "enabled")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-columns/if-exists", info.getSql());
    }

    @Test
    public void shouldMapCamelCaseColumnsToSnakeCase() {
        Operation operation = DropColumns.builder("schemawizard", "users")
                .columns("firstName", "lastName")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-columns/map-camel-case-to-snake-case", info.getSql());
    }
}
