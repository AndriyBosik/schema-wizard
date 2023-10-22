package com.example.migration;

import com.example.builder.model.MigrationContext;
import com.example.builder.operation.Operation;
import com.example.builder.column.ColumnBuilder;
import com.example.Migration;
import com.example.builder.migration.CreateTableMigrationBuilder;
import com.example.builder.migration.DropTableMigrationBuilder;

import java.util.List;

public class UsersTableMigration implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CreateTableMigrationBuilder.builder(
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
        return DropTableMigrationBuilder.builder("example", "users")
                .checkIfExists(true)
                .build();
    }
}
