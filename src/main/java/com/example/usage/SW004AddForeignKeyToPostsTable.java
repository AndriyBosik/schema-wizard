package com.example.usage;

import com.example.migration.Migration;
import com.example.migration.annotation.SWName;
import com.example.migration.builder.operation.AddForeignKey;
import com.example.migration.builder.operation.DropForeignKey;
import com.example.migration.model.MigrationContext;
import com.example.migration.operation.Operation;

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
