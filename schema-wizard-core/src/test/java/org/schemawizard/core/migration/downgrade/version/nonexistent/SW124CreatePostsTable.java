package org.schemawizard.core.migration.downgrade.version.nonexistent;

import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.builder.column.ColumnBuilder;
import org.schemawizard.core.migration.builder.operation.CreateTable;
import org.schemawizard.core.migration.builder.operation.DropTable;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

public class SW124CreatePostsTable implements Migration {
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
