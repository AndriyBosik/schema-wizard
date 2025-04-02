package org.schemawizard.core.generic;

import org.junit.jupiter.api.Test;
import org.schemawizard.core.config.GenericTest;
import org.schemawizard.core.config.extension.DisableFor;
import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.migration.builder.column.ColumnBuilder;
import org.schemawizard.core.migration.builder.operation.CreateTable;
import org.schemawizard.core.migration.metadata.ReferentialAction;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.Operation;

import java.util.List;

public class CreateTableTest extends GenericTest {
    @Test
    @DisableFor(DatabaseProvider.MYSQL)
    public void shouldGenerateCreateTableWithSingleConstraintsAndIntervalYearToMonthCustomType() {
        Operation operation = CreateTable.builder("schemawizard", "posts", factory -> new Object() {
                    ColumnBuilder id = factory.newInteger();
                    ColumnBuilder user = factory.newInteger("userId").nullable(false);
                    ColumnBuilder age = factory.newInteger().nullable(false);
                    ColumnBuilder title = factory.newText().nullable(false).maxLength(300);
                    ColumnBuilder rate = factory.newDecimal().nullable(false).defaultValue(0);
                    ColumnBuilder createdDate = factory.generic().nullable(false).type("INTERVAL YEAR TO MONTH");
                })
                .primaryKey(table -> table.id)
                .foreignKey("fk_posts_user_id", fk -> fk.column(table -> table.user)
                        .foreignSchema("schemawizard")
                        .foreignTable("users")
                        .foreignColumn("id"))
                .unique("unq_title", table -> table.title)
                .check("rate >= 0 AND rate <= 100")
                .check("chk_user_age_greater_than_zero", "age > 0")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("create-table/single-constraints/interval-year-to-month", info.getSql());
    }

    @Test
    @DisableFor({DatabaseProvider.POSTGRESQL, DatabaseProvider.ORACLE})
    public void shouldGenerateCreateTableWithSingleConstraintsAndENumCustomType() {
        Operation operation = CreateTable.builder("schemawizard", "posts", factory -> new Object() {
                    ColumnBuilder id = factory.newInteger();
                    ColumnBuilder user = factory.newInteger("userId").nullable(false);
                    ColumnBuilder age = factory.newInteger().nullable(false);
                    ColumnBuilder title = factory.newText().nullable(false).maxLength(300);
                    ColumnBuilder rate = factory.newDecimal().nullable(false).defaultValue(0);
                    ColumnBuilder createdDate = factory.generic().nullable(false).type("ENUM('YESTERDAY', 'TODAY', 'TOMORROW')");
                })
                .primaryKey(table -> table.id)
                .foreignKey("fk_posts_user_id", fk -> fk.column(table -> table.user)
                        .foreignSchema("schemawizard")
                        .foreignTable("users")
                        .foreignColumn("id"))
                .unique("unq_title", table -> table.title)
                .check("rate >= 0 AND rate <= 100")
                .check("chk_user_age_greater_than_zero", "age > 0")
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("create-table/single-constraints/enum", info.getSql());
    }

    @Test
    @DisableFor(DatabaseProvider.MYSQL)
    public void shouldGenerateCreateTableWithCompositeConstraintsAndIntervalYearToMonthCustomType() {
        Operation operation = CreateTable.builder("schemawizard", "posts", factory -> new Object() {
                    ColumnBuilder user = factory.newInteger("userId").nullable(false);
                    ColumnBuilder userEmail = factory.newText().nullable(false).maxLength(50);
                    ColumnBuilder title = factory.newText().nullable(false).maxLength(300);
                    ColumnBuilder rate = factory.newDecimal().nullable(false).defaultValue(0);
                    ColumnBuilder createdDate = factory.generic().nullable(false).type("INTERVAL YEAR TO MONTH");
                })
                .compositePrimaryKey("pk_posts_user_id_created_date", table -> List.of(table.user, table.createdDate))
                .compositeForeignKey("fk_posts_user_id_user_email", fk -> fk.columns(table -> List.of(table.user, table.userEmail))
                        .foreignSchema("schemawizard")
                        .foreignTable("users")
                        .foreignColumns("id", "email"))
                .compositeUnique("unq_user_id_title", table -> List.of(table.user, table.title))
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("create-table/multiple-constraints/interval-year-to-month", info.getSql());
    }

    @Test
    @DisableFor({DatabaseProvider.POSTGRESQL, DatabaseProvider.ORACLE})
    public void shouldGenerateCreateTableWithCompositeConstraintsAndEnumCustomType() {
        Operation operation = CreateTable.builder("schemawizard", "posts", factory -> new Object() {
                    ColumnBuilder user = factory.newInteger("userId").nullable(false);
                    ColumnBuilder userEmail = factory.newText().nullable(false).maxLength(50);
                    ColumnBuilder title = factory.newText().nullable(false).maxLength(300);
                    ColumnBuilder rate = factory.newDecimal().nullable(false).defaultValue(0);
                    ColumnBuilder createdDate = factory.generic().nullable(false).type("ENUM('YESTERDAY', 'TODAY', 'TOMORROW')");
                })
                .compositePrimaryKey("pk_posts_user_id_created_date", table -> List.of(table.user, table.createdDate))
                .compositeForeignKey("fk_posts_user_id_user_email", fk -> fk.columns(table -> List.of(table.user, table.userEmail))
                        .foreignSchema("schemawizard")
                        .foreignTable("users")
                        .foreignColumns("id", "email"))
                .compositeUnique("unq_user_id_title", table -> List.of(table.user, table.title))
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("create-table/multiple-constraints/enum", info.getSql());
    }

    @Test
    public void shouldGenerateWithoutConstraints() {
        Operation operation = CreateTable.builder("users", factory -> new Object() {
                    ColumnBuilder id = factory.newInteger().nullable(false);
                    ColumnBuilder email = factory.newText().nullable(false).maxLength(50);
                })
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("create-table/without-constraints", info.getSql());
    }

    @Test
    public void shouldGenerateWithIfNotExists() {
        Operation operation = CreateTable.builder("users", factory -> new Object() {
                    ColumnBuilder id = factory.newInteger().nullable(false);
                    ColumnBuilder email = factory.newText().nullable(false).maxLength(50);
                })
                .ifNotExists()
                .primaryKey(table -> table.id)
                .build();

        MigrationInfo info = operationResolverService.resolve(operation);
        assertQuery("create-table/if-not-exists", info.getSql());
    }

    @Test
    public void shouldGenerateWithReferentialActions() {
        Operation operation = CreateTable.builder("posts", factory -> new Object() {
                    ColumnBuilder id = factory.newInteger().nullable(false);
                    ColumnBuilder user = factory.newInteger("user_id").nullable(true);
                    ColumnBuilder email = factory.newText().nullable(false).maxLength(50);
                })
                .ifNotExists()
                .primaryKey(table -> table.id)
                .foreignKey("fk_posts_user_id", fk -> fk.column(table -> table.user)
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
