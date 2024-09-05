package com.example.migration;

import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.annotation.SWName;
import org.schemawizard.core.migration.builder.column.ColumnBuilder;
import org.schemawizard.core.migration.builder.operation.CreateTable;
import org.schemawizard.core.migration.builder.operation.DropTable;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

@SWName("Create test table")
public class SW005CreateTestTable implements Migration {

    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder(
                        "SCHEMAWIZARD",
                        "test",
                        factory -> new Object() {
                            ColumnBuilder firstName = factory.newInteger("id").nullable(false);
                            ColumnBuilder middleName = factory.newText("middle_name").nullable(true);
                            ColumnBuilder lastName = factory.newText("last_name").nullable(false);
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
