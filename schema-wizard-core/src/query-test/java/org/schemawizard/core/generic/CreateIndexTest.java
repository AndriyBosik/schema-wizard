package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.migration.builder.operation.AddUnique;
import org.schemawizard.core.migration.builder.operation.CreateIndex;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

public class CreateIndexTest extends GenericTest {
    @Test
    public void shouldGenerateCreateIndexForSingleColumn() {
        Operation operation = CreateIndex.builder("schemawizard", "users")
                .name("idx_users_email")
                .columns("email")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("create-index/single-column", info.getSql());
    }

    @Test
    public void shouldGenerateCreateIndexForMultipleColumns() {
        Operation operation = CreateIndex.builder("users")
                .name("idx_users_first_name_last_name")
                .columns("first_name", "last_name")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("create-index/multiple-columns", info.getSql());
    }

    @Test
    public void shouldGenerateCreateIndexWithUsing() {
        Operation operation = CreateIndex.builder("users")
                .name("idx_users_email")
                .columns("email")
                .using("hash")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("create-index/using", info.getSql());
    }

    @Test
    public void shouldGenerateCreateIndexIfNotExists() {
        Operation operation = CreateIndex.builder("users")
                .name("idx_users_first_name_last_name")
                .columns("first_name", "last_name")
                .ifNotExists()
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("create-index/if-not-exists", info.getSql());
    }

    @Test
    public void shouldMapCamelCaseColumnsToSnakeCase() {
        Operation operation = CreateIndex.builder("users")
                .name("idx_users_first_name_last_name")
                .columns("firstName", "lastName")
                .ifNotExists()
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("create-index/map-camel-case-to-snake-case", info.getSql());
    }
}
