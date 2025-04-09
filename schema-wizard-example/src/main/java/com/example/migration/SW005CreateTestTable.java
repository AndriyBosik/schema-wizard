package com.example.migration;

import io.github.andriybosik.schemawizard.core.migration.Migration;
import io.github.andriybosik.schemawizard.core.migration.annotation.SWName;
import io.github.andriybosik.schemawizard.core.migration.builder.column.ColumnBuilder;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.CreateTable;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.DropTable;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationContext;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

@SWName("Create test table")
public class SW005CreateTestTable implements Migration {

    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder(
                "SCHEMAWIZARD",
                "test",
                factory -> new Object() {
                    public ColumnBuilder firstName() {
                        return factory.newText("first_name").nullable(false).maxLength(100);
                    }

                    public ColumnBuilder middleName() {
                        return factory.newText("middle_name").nullable(true);
                    }

                    public ColumnBuilder lastName() {
                        return factory.newText("last_name").nullable(false);
                    }
                })
            .ifNotExists()
            .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropTable.builder("SCHEMAWIZARD", "test")
            .ifExists()
            .build();
    }
}
