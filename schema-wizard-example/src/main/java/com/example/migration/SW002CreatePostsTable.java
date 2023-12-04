package com.example.migration;

import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.builder.column.ColumnBuilder;
import org.schemawizard.core.migration.builder.operation.CreateTable;
import org.schemawizard.core.migration.builder.operation.DropTable;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

public class SW002CreatePostsTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder(
                        "posts",
                        factory -> new Object() {
                            public ColumnBuilder id() {
                                return factory.integer("id").nullable(false);
                            }

                            public ColumnBuilder title() {
                                return factory.text("title").nullable(false);
                            }

                            public ColumnBuilder description() {
                                return factory.bool("description").nullable(false);
                            }

                            public ColumnBuilder userId() {
                                return factory.integer("user_id").nullable(false);
                            }
                        })
                .ifNotExists()
                .primaryKey("pk_posts", table -> table.id())
                .foreignKey(fk -> fk.column(table -> table.userId())
                        .foreignSchema("public")
                        .foreignTable("users")
                        .foreignColumn("id"))
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropTable.builder("public", "posts").build();
    }
}
