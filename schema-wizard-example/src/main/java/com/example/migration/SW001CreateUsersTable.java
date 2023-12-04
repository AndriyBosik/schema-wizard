package com.example.migration;

import org.schemawizard.core.migration.annotation.SWName;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;
import org.schemawizard.core.migration.builder.column.ColumnBuilder;
import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.builder.operation.CreateTable;
import org.schemawizard.core.migration.builder.operation.DropTable;

import java.util.List;

@SWName("Create users table")
public class SW001CreateUsersTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder(
                        "public",
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
                .ifNotExists()
                .primaryKey("pk_users", table -> table.id())
                .unique(table -> table.email())
                .compositeUnique("unq_first_name_last_name", table -> List.of(table.firstName(), table.lastName()))
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropTable.builder("public", "users")
                .ifExists()
                .build();
    }
}
