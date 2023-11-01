package com.example.usage;

import com.example.migration.Migration;
import com.example.migration.builder.column.ColumnBuilder;
import com.example.migration.builder.operation.*;
import com.example.migration.model.MigrationContext;
import com.example.migration.operation.Operation;

public class SW005CreateRolesTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return Composite.builder()
                .add(CreateTable.builder("example", "roles", factory -> new Object() {
                            public ColumnBuilder id() {
                                return factory.integer("id").nullable(false);
                            }

                            public ColumnBuilder title() {
                                return factory.text("title").nullable(false);
                            }
                        })
                        .primaryKey(pk -> pk.id())
                        .build())
                .add(CreateTable.builder("example", "users_roles", factory -> new Object() {
                            public ColumnBuilder id() {
                                return factory.integer("id").nullable(false);
                            }

                            public ColumnBuilder userId() {
                                return factory.integer("user_id").nullable(false);
                            }

                            public ColumnBuilder roleId() {
                                return factory.integer("role_id").nullable(false);
                            }
                        })
                        .foreignKey(fk -> fk.name("fk_users_roles_role_id")
                                .column(table -> table.roleId())
                                .foreignSchema("example")
                                .foreignTable("roles")
                                .foreignColumn("id"))
                        .build())
                .add(AddPrimaryKey.builder("example", "users_roles")
                        .name("pk_users_roles")
                        .columns("id")
                        .build())
                .add(AddForeignKey.builder("example", "users_roles")
                        .name("fk_users_roles_user_id")
                        .columns("user_id")
                        .foreignSchema("example")
                        .foreignTable("users")
                        .foreignColumns("id")
                        .build())
                .add(AddUnique.builder("example", "users_roles")
                        .name("unq_users_roles_user_id_role_id")
                        .columns("user_id", "role_id")
                        .build())
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return Composite.builder()
                .add(DropTable.builder("example", "users_roles").build())
                .add(DropTable.builder("example", "roles").build())
                .build();
    }
}
