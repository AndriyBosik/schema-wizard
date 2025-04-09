package io.github.andriybosik.schemawizard.core.migration.validation.incorrectmigrationnamepattern;

import io.github.andriybosik.schemawizard.core.migration.Migration;
import io.github.andriybosik.schemawizard.core.migration.builder.column.ColumnBuilder;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.CreateTable;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.DropTable;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationContext;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

public class SWDescriptionAndVersion001 implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder("people", factory -> new Object() {
                    ColumnBuilder id = factory.newInteger("id").nullable(false);
                })
                .primaryKey(table -> table.id)
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropTable.builder("people").build();
    }
}
