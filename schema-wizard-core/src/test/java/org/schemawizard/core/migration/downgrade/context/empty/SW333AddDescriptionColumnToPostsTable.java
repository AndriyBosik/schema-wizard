package org.schemawizard.core.migration.downgrade.context.empty;

import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.builder.operation.AddColumns;
import org.schemawizard.core.migration.builder.operation.DropColumns;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

import java.util.List;

public class SW333AddDescriptionColumnToPostsTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return AddColumns.builder(
                        "posts",
                        factory -> List.of(
                                factory.integer("description").nullable(false)))
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropColumns.builder("posts")
                .columns("description")
                .build();
    }
}
