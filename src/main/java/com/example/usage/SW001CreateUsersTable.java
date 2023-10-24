package com.example.usage;

import com.example.migration.annotation.SWName;
import com.example.migration.model.MigrationContext;
import com.example.migration.operation.Operation;
import com.example.migration.builder.column.ColumnBuilder;
import com.example.migration.Migration;
import com.example.migration.builder.operation.CreateTable;
import com.example.migration.builder.operation.DropTable;

import java.util.List;

@SWName("Create users table")
public class SW001CreateUsersTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder(
                        "example",
                        "users",
                        factory -> new Object() {
                            public ColumnBuilder id() {
                                return factory.integer("id").nullable(false);
                            }

                            public ColumnBuilder email() {
                                return factory.text("email").nullable(false);
                            }

                            public ColumnBuilder enabled() {
                                return factory.bool("enabled").nullable(false);
                            }

                            public ColumnBuilder firstName() {
                                return factory.text("first_name").nullable(false);
                            }

                            public ColumnBuilder middleName() {
                                return factory.text("middle_name").nullable(true);
                            }

                            public ColumnBuilder lastName() {
                                return factory.text("last_name").nullable(false);
                            }
                        })
                .checkIfNotExists(true)
                .primaryKey(table -> table.id())
                .unique(table -> table.email())
                .compositeUnique("unq_first_name_last_name", table -> List.of(table.firstName(), table.lastName()))
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropTable.builder("example", "users")
                .checkIfExists(true)
                .build();
    }
}
