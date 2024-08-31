package org.schemawizard.core.migration.subsequent;

import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.annotation.SWName;
import org.schemawizard.core.migration.builder.column.ColumnBuilder;
import org.schemawizard.core.migration.builder.operation.CreateTable;
import org.schemawizard.core.migration.builder.operation.DropTable;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

@SWName("Create posts table")
public class SW444CreatePostsTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder("posts", factory -> new Object() {
                    public ColumnBuilder id() {
                        return factory.integer("id");
                    }

                    public ColumnBuilder email() {
                        return factory.text("title").nullable(false);
                    }
                })
                .primaryKey(table -> table.id())
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropTable.builder("posts").build();
    }
}
