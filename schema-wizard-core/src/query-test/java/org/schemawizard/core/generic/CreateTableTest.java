package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.exception.InvalidMigrationDefinitionException;
import org.schemawizard.core.migration.builder.column.ColumnBuilder;
import org.schemawizard.core.migration.builder.operation.CreateTable;
import org.schemawizard.core.migration.metadata.ReferentialAction;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateTableTest extends GenericTest {
    @Test
    public void shouldGenerateCreateTableWithSingleConstraints() {
        Operation operation = CreateTable.builder("schemawizard", "posts", factory -> new Object() {
                    public ColumnBuilder id() {
                        return factory.newInteger("id");
                    }

                    public ColumnBuilder userId() {
                        return factory.newInteger("user_id")
                                .nullable(false);
                    }

                    public ColumnBuilder age() {
                        return factory.newInteger("age")
                                .nullable(false);
                    }

                    public ColumnBuilder title() {
                        return factory.newText("title")
                                .nullable(false)
                                .maxLength(300);
                    }

                    public ColumnBuilder rate() {
                        return factory.newDecimal("rate")
                                .nullable(false)
                                .defaultValue(0);
                    }

                    public ColumnBuilder createdDate() {
                        return factory.generic("created_date")
                                .nullable(false)
                                .type("INTERVAL YEAR TO MONTH");
                    }
                })
                .primaryKey(table -> table.id())
                .foreignKey("fk_posts_user_id", fk -> fk.column(table -> table.userId())
                        .foreignSchema("schemawizard")
                        .foreignTable("users")
                        .foreignColumn("id"))
                .unique("unq_title", table -> table.title())
                .check("rate >= 0 AND rate <= 100")
                .check("chk_user_age_greater_than_zero", "age > 0")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("create-table/single-constraints", info.getSql());
    }

    @Test
    public void shouldThrowExceptionWhenIfNotExistsUsedForColumn() {
        CreateTable<Object> operationBuilder = CreateTable.builder("posts", factory -> new Object() {
            public ColumnBuilder id() {
                return factory.newInteger("id")
                        .ifNotExists();
            }

            public ColumnBuilder title() {
                return factory.newText("title")
                        .nullable(false)
                        .maxLength(300)
                        .ifNotExists();
            }
        });

        InvalidMigrationDefinitionException exception = assertThrows(InvalidMigrationDefinitionException.class, operationBuilder::build);
        assertEquals("[IF NOT EXISTS] clause not supported", exception.getMessage());
    }

    @Test
    public void shouldGenerateCreateTableWithCompositeConstraints() {
        Operation operation = CreateTable.builder("schemawizard", "posts", factory -> new Object() {
                    public ColumnBuilder userId() {
                        return factory.newInteger("user_id")
                                .nullable(false);
                    }

                    public ColumnBuilder userEmail() {
                        return factory.newText("user_email")
                                .nullable(false)
                                .maxLength(50);
                    }

                    public ColumnBuilder title() {
                        return factory.newText("title")
                                .nullable(false)
                                .maxLength(300);
                    }

                    public ColumnBuilder rate() {
                        return factory.newDecimal("rate")
                                .nullable(false)
                                .defaultValue(0);
                    }

                    public ColumnBuilder createdDate() {
                        return factory.generic("created_date")
                                .nullable(false)
                                .type("INTERVAL YEAR TO MONTH");
                    }
                })
                .compositePrimaryKey("pk_posts_user_id_created_date", table -> List.of(table.userId(), table.createdDate()))
                .compositeForeignKey("fk_posts_user_id_user_email", fk -> fk.columns(table -> List.of(table.userId(), table.userEmail()))
                        .foreignSchema("schemawizard")
                        .foreignTable("users")
                        .foreignColumns("id", "email"))
                .compositeUnique("unq_user_id_title", table -> List.of(table.userId(), table.title()))
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("create-table/multiple-constraints", info.getSql());
    }

    @Test
    public void shouldGenerateWithoutConstraints() {
        Operation operation = CreateTable.builder("users", factory -> new Object() {
                    public ColumnBuilder id() {
                        return factory.newInteger("id")
                                .nullable(false);
                    }

                    public ColumnBuilder email() {
                        return factory.newText("email")
                                .nullable(false)
                                .maxLength(50);
                    }
                })
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("create-table/without-constraints", info.getSql());
    }

    @Test
    public void shouldGenerateWithIfNotExists() {
        Operation operation = CreateTable.builder("users", factory -> new Object() {
                    public ColumnBuilder id() {
                        return factory.newInteger("id")
                                .nullable(false);
                    }

                    public ColumnBuilder email() {
                        return factory.newText("email")
                                .nullable(false)
                                .maxLength(50);
                    }
                })
                .ifNotExists()
                .primaryKey(table -> table.id())
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("create-table/if-not-exists", info.getSql());
    }

    @Test
    public void shouldGenerateWithReferentialActions() {
        Operation operation = CreateTable.builder("posts", factory -> new Object() {
                    public ColumnBuilder id() {
                        return factory.newInteger("id")
                                .nullable(false);
                    }

                    public ColumnBuilder userId() {
                        return factory.newInteger("user_id")
                                .nullable(true);
                    }

                    public ColumnBuilder email() {
                        return factory.newText("email")
                                .nullable(false)
                                .maxLength(50);
                    }
                })
                .ifNotExists()
                .primaryKey(table -> table.id())
                .foreignKey("fk_posts_user_id", fk -> fk.column(table -> table.userId())
                        .foreignSchema("schemawizard")
                        .foreignTable("users")
                        .foreignColumn("id")
                        .onDelete(ReferentialAction.CASCADE)
                        .onUpdate(ReferentialAction.SET_NULL))
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("create-table/referential-actions", info.getSql());
    }
}
