package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.migration.builder.operation.DropUnique;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

public class DropUniqueTest extends GenericTest {
    @Test
    public void shouldGenerateDropUniqueConstraintForTable() {
        Operation operation = DropUnique.builder("users")
                .name("unq_users_first_name_last_name")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-unique/table", info.getSql());
    }

    @Test
    public void shouldGenerateDropUniqueConstraintForSchemaAndTableAndName() {
        Operation operation = DropUnique.builder("schemawizard", "users")
                .name("unq_users_first_name_last_name")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-unique/schema-and-table", info.getSql());
    }

    @Test
    public void shouldGenerateDropUniqueIfExists() {
        Operation operation = DropUnique.builder("users")
                .name("unq_email")
                .ifExists()
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-unique/if-exists", info.getSql());
    }
}
