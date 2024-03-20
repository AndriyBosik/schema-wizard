package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.migration.builder.operation.DropPrimaryKey;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

public class DropPrimaryKeyTest extends GenericTest {
    @Test
    public void shouldGenerateDropPrimaryKeyConstraintForTable() {
        Operation operation = DropPrimaryKey.builder("users")
                .name("pk_users_id")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-primary-key/table", info.getSql());
    }

    @Test
    public void shouldGenerateDropPrimaryConstraintForSchemaAndTableAndName() {
        Operation operation = DropPrimaryKey.builder("schemawizard", "users")
                .name("pk_users_id")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-primary-key/schema-and-table", info.getSql());
    }

    @Test
    public void shouldGenerateDropPrimaryKeyIfExists() {
        Operation operation = DropPrimaryKey.builder("users")
                .name("pk_users_id")
                .ifExists()
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-primary-key/if-exists", info.getSql());
    }
}
