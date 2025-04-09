package com.example.migration;

import io.github.andriybosik.schemawizard.core.migration.Migration;
import io.github.andriybosik.schemawizard.core.migration.annotation.SWName;
import io.github.andriybosik.schemawizard.core.migration.builder.column.ColumnBuilder;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.CreateTable;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.DropTable;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationContext;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

import java.util.List;

@SWName("Create users table")
public class SW001CreateUsersTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder(
                        "SCHEMAWIZARD",
                        "users",
                        factory -> new Object() {
                            ColumnBuilder id = factory.newInteger().nullable(false);
                            ColumnBuilder email = factory.newText().nullable(false);
                            ColumnBuilder enabled = factory.newBool().nullable(false);
                            ColumnBuilder firstName = factory.newText().nullable(false);
                            ColumnBuilder middleName = factory.newText().nullable(true);
                            ColumnBuilder lastName = factory.newText().nullable(false);
                        })
                .ifNotExists()
                .primaryKey("pk_users", table -> table.id)
                .unique(table -> table.email)
                .compositeUnique("unq_first_name_last_name", table -> List.of(table.firstName, table.lastName))
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropTable.builder("SCHEMAWIZARD", "users")
                .ifExists()
                .build();
    }
}
