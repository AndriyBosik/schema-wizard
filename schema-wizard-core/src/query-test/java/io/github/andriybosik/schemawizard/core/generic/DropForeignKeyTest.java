package io.github.andriybosik.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import io.github.andriybosik.schemawizard.core.config.GenericTest;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.DropForeignKey;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationInfo;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

public class DropForeignKeyTest extends GenericTest {
    @Test
    public void shouldGenerateDropForeignKeyConstraintForTable() {
        Operation operation = DropForeignKey.builder("posts")
                .name("fk_posts_user_id")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-foreign-key/table", info.getSql());
    }

    @Test
    public void shouldGenerateDropForeignConstraintForSchemaAndTableAndName() {
        Operation operation = DropForeignKey.builder("example", "posts")
                .name("fk_posts_user_id")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-foreign-key/schema-and-table", info.getSql());
    }

    @Test
    public void shouldGenerateDropForeignKeyIfExists() {
        Operation operation = DropForeignKey.builder("posts")
                .name("fk_posts_user_id")
                .ifExists()
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("drop-foreign-key/if-exists", info.getSql());
    }
}
