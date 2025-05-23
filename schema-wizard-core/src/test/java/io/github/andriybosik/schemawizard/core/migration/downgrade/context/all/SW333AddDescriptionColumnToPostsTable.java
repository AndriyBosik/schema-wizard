package io.github.andriybosik.schemawizard.core.migration.downgrade.context.all;

import io.github.andriybosik.schemawizard.core.migration.Migration;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.AddColumns;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.DropColumns;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationContext;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

import java.util.List;

public class SW333AddDescriptionColumnToPostsTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return AddColumns.builder(
                        "posts",
                        factory -> List.of(
                                factory.newInteger("description").nullable(false)))
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropColumns.builder("posts")
                .columns("description")
                .build();
    }
}
