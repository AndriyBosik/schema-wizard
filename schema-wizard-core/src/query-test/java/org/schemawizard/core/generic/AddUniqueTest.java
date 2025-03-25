package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.migration.builder.operation.AddUnique;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

public class AddUniqueTest extends GenericTest {
    @Test
    public void shouldGenerateAddUniqueConstraintForSingleColumn() {
        Operation operation = AddUnique.builder("users")
                .name("unq_users_email")
                .columns("email")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-unique/single-column", info.getSql());
    }

    @Test
    public void shouldGenerateAddUniqueConstraintForMultipleColumns() {
        Operation operation = AddUnique.builder("users")
                .name("unq_users_first_name_last_name")
                .columns("first_name", "last_name")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-unique/multiple-columns", info.getSql());
    }

    @Test
    public void shouldGenerateAddUniqueConstraintForMultipleColumnsWithSchema() {
        Operation operation = AddUnique.builder("schemawizard", "users")
                .name("unq_users_first_name_last_name")
                .columns("first_name", "last_name")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-unique/multiple-columns-with-schema", info.getSql());
    }

    @Test
    public void shouldMapCamelCaseColumnsToSnakeCase() {
        Operation operation = AddUnique.builder("schemawizard", "users")
                .name("unq_users_first_name_last_name")
                .columns("firstName", "lastName")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-unique/map-camel-case-to-snake-case", info.getSql());
    }
}
