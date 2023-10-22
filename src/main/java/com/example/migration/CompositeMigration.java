package com.example.migration;

import com.example.Migration;
import com.example.builder.column.ColumnBuilder;
import com.example.builder.migration.*;
import com.example.builder.model.MigrationContext;
import com.example.builder.operation.Operation;

import java.util.List;

public class CompositeMigration implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CompositeOperationBuilder.builder()
                .add(CreateTableMigrationBuilder.builder("example", "posts", factory -> new Object() {
                    public ColumnBuilder id() {
                        return factory.integer("id").nullable(false);
                    }

                    public ColumnBuilder title() {
                        return factory.text("title").nullable(false);
                    }

                    public ColumnBuilder description() {
                        return factory.text("description").nullable(false);
                    }
                }).build())
                .add(AddColumnsMigrationBuilder.builder("example", "posts", factory -> List.of(
                        factory.integer("views_count").nullable(false).defaultValue(0),
                        factory.text("author_note").nullable(true)
                )).build())
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return CompositeOperationBuilder.builder()
                .add(DropColumnsMigrationBuilder.builder("example", "posts")
                        .columns("author_note", "views_count")
                        .build())
                .add(DropTableMigrationBuilder.builder("example", "posts").build())
                .build();
    }
}
