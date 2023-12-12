package com.example.migration;

import com.example.custom.JsonColumnBuilder;
import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.builder.column.ColumnBuilder;
import org.schemawizard.core.migration.builder.operation.CreateTable;
import org.schemawizard.core.migration.builder.operation.DropTable;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

public class SW005CreateExampleTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder(
                        "public",
                        "example",
                        factory -> new Object() {
                            public ColumnBuilder id() {
                                return factory.integer("id").nullable(false);
                            }

                            public ColumnBuilder title() {
                                return factory.text("title").nullable(false);
                            }

                            public ColumnBuilder details() {
                                return new JsonColumnBuilder("details");
//                                return ColumnUtils.json("details").apply(factory);
                            }
                        })
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropTable.builder("public", "example").build();
    }
}
