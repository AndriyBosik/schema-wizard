package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.migration.builder.operation.AddForeignKey;
import org.schemawizard.core.migration.metadata.ReferentialAction;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.AddForeignKeyOperation;

public class AddForeignKeyTest extends GenericTest {
    @Test
    public void shouldGenerateForTable() {
        AddForeignKeyOperation operation = AddForeignKey.builder("posts")
                .name("fk_posts_user_id")
                .columns("user_id")
                .foreignTable("users")
                .foreignColumns("id")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-foreign-key/table", info.getSql());
    }

    @Test
    public void shouldGenerateForTableAndSchema() {
        AddForeignKeyOperation operation = AddForeignKey.builder("example", "posts")
                .name("fk_posts_user_id")
                .columns("user_id")
                .foreignSchema("schemawizard")
                .foreignTable("users")
                .foreignColumns("id")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-foreign-key/schema-and-table", info.getSql());
    }

    @Test
    public void shouldGenerateForMultipleColumns() {
        AddForeignKeyOperation operation = AddForeignKey.builder("posts")
                .name("fk_posts_user_id_user_email")
                .columns("user_id", "user_email")
                .foreignTable("users")
                .foreignColumns("id", "email")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-foreign-key/multiple-columns", info.getSql());
    }

    @Test
    public void shouldGenerateWithReferentialActions() {
        AddForeignKeyOperation operation = AddForeignKey.builder("posts")
                .name("fk_posts_user_id")
                .columns("user_id")
                .foreignTable("users")
                .foreignColumns("id")
                .onDelete(ReferentialAction.CASCADE)
                .onUpdate(ReferentialAction.SET_NULL)
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("add-foreign-key/referential-actions", info.getSql());
    }
}
