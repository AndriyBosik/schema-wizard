package com.example.migration;

import io.github.andriybosik.schemawizard.core.migration.Migration;
import io.github.andriybosik.schemawizard.core.migration.builder.column.ColumnBuilder;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.CreateTable;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.DropTable;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationContext;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

public class SW002CreatePostsTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder(
                        "SCHEMAWIZARD",
                        "posts",
                        factory -> new Object() {
                            ColumnBuilder id = factory.newInteger().nullable(false);
                            ColumnBuilder title = factory.newText().nullable(false);
                            ColumnBuilder description = factory.newBool().nullable(false);
                            ColumnBuilder userId = factory.newInteger().nullable(false);
                        })
                .ifNotExists()
                .primaryKey("pk_posts", table -> table.id)
                .foreignKey(fk -> fk.column(table -> table.userId)
                        .foreignSchema("SCHEMAWIZARD")
                        .foreignTable("users")
                        .foreignColumn("id"))
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropTable.builder("SCHEMAWIZARD", "posts").build();
    }
}
