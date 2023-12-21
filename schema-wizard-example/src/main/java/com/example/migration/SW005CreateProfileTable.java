package com.example.migration;

import com.example.extension.ColumnUtils;
import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.builder.column.ColumnBuilder;
import org.schemawizard.core.migration.builder.operation.CreateTable;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

public class SW005CreateProfileTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder("schemawizard", "profile", factory -> new Object() {
            public ColumnBuilder id() {
                return factory.integer("id").nullable(false);
            }

            public ColumnBuilder details() {
                return ColumnUtils.json(factory)
                        .name("details");
            }
        }).build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return null;
    }
}
