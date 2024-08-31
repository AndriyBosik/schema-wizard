package org.schemawizard.core.migration.emptyhistory;

import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.builder.column.ColumnBuilder;
import org.schemawizard.core.migration.builder.operation.CreateTable;
import org.schemawizard.core.migration.builder.operation.DropTable;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

public class SW001CreatePeopleTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder("people", factory -> new Object() {
                    public ColumnBuilder id() {
                        return factory.integer("id");
                    }

                    public ColumnBuilder email() {
                        return factory.text("email").nullable(false);
                    }
                })
                .primaryKey(table -> table.id())
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropTable.builder("people").build();
    }
}
