package io.github.andriybosik.schemawizard.core.migration.upgrade.unordered;

import io.github.andriybosik.schemawizard.core.migration.Migration;
import io.github.andriybosik.schemawizard.core.migration.annotation.SWName;
import io.github.andriybosik.schemawizard.core.migration.builder.column.ColumnBuilder;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.CreateTable;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.DropTable;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationContext;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

@SWName("Create posts table")
public class SW002CreatePostsTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder("posts", factory -> new Object() {
                    ColumnBuilder id = factory.newInteger();
                    ColumnBuilder title = factory.newText().nullable(false);
                })
                .primaryKey(table -> table.id)
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropTable.builder("posts").build();
    }
}
