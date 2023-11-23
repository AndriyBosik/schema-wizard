package org.schemawizard.core.usage;

import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.builder.column.ColumnBuilder;
import org.schemawizard.core.migration.builder.operation.*;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;
import org.schemawizard.core.migration.metadata.ReferentialAction;

import java.util.List;

public class SW002CreatePostsTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return Composite.builder()
                .add(CreateTable.builder("example", "posts", factory -> new Object() {
                            public ColumnBuilder id() {
                                return factory.integer("id").nullable(false);
                            }

                            public ColumnBuilder userId() {
                                return factory.integer("user_id").nullable(false);
                            }

                            public ColumnBuilder reviewerId() {
                                return factory.integer("reviewer_id").nullable(false);
                            }

                            public ColumnBuilder topicId() {
                                return factory.integer("topic_id").nullable(false);
                            }

                            public ColumnBuilder title() {
                                return factory.text("title").nullable(false);
                            }

                            public ColumnBuilder description() {
                                return factory.text("description").nullable(false);
                            }
                        })
                        .foreignKey(fk -> fk.name("fk_test_name")
                                .column(table -> table.userId())
                                .foreignTable("users")
                                .foreignColumn("id")
                                .onUpdate(ReferentialAction.CASCADE))
                        .compositeForeignKey(fk -> fk.name("fk_composite_foreign_key")
                                .columns(table -> List.of(table.reviewerId(), table.topicId()))
                                .foreignTable("delegates")
                                .foreignColumns("reviewer_id", "topic_id")
                                .onUpdate(ReferentialAction.NO_ACTION)
                                .onDelete(ReferentialAction.CASCADE))
                        .build())
                .add(AddColumns.builder("example", "posts", factory -> List.of(
                        factory.integer("views_count").nullable(false).defaultValue(0),
                        factory.text("author_note").nullable(true)
                )).build())
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return Composite.builder()
                .add(DropColumns.builder("example", "posts")
                        .columns("author_note", "views_count")
                        .build())
                .add(DropTable.builder("example", "posts").build())
                .build();
    }
}
