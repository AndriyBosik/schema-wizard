package org.schemawizard.core.migration;

import org.schemawizard.core.migration.builder.column.ColumnBuilder;
import org.schemawizard.core.migration.builder.operation.CreateTable;
import org.schemawizard.core.migration.builder.operation.DropTable;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

import java.util.List;

public class SW001CreateUsersTable implements Migration {

    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder(
                        "SCHEMAWIZARD",
                        "users",
                        factory -> new Object() {
                            public ColumnBuilder id() {
                                return factory.newInteger("id").nullable(false);
                            }

                            public ColumnBuilder email() {
                                return factory.newText("email").nullable(false);
                            }

                            public ColumnBuilder enabled() {
                                return factory.newBool("enabled").nullable(false);
                            }

                            public ColumnBuilder firstName() {
                                return factory.newText("first_name").nullable(false);
                            }

                            public ColumnBuilder middleName() {
                                return factory.newText("middle_name").nullable(true);
                            }

                            public ColumnBuilder lastName() {
                                return factory.newText("last_name").nullable(false);
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
        return DropTable.builder("SCHEMAWIZARD", "users")
                .ifExists()
                .build();
    }
}
