package org.schemawizard.core.usage;

import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.annotation.SWName;
import org.schemawizard.core.migration.builder.operation.AddForeignKey;
import org.schemawizard.core.migration.builder.operation.DropForeignKey;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

@SWName("Foreign key operation test")
public class SW004AddForeignKeyToPostsTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return AddForeignKey.builder("posts")
                .name("fk_posts_another_table")
                .columns("user_id", "user_email", "user_first_name")
                .foreignTable("users")
                .foreignColumns("id", "email", "first_name")
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropForeignKey.builder("posts")
                .name("fk_posts_another_table")
                .build();
    }
}
