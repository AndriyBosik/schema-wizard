package io.github.andriybosik.schemawizard.core.migration.validation.constructionwithparams;

import io.github.andriybosik.schemawizard.core.migration.Migration;
import io.github.andriybosik.schemawizard.core.migration.builder.column.ColumnBuilder;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.CreateTable;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.DropTable;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationContext;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

public class SW001CreatePeopleTable implements Migration {
    public SW001CreatePeopleTable(String message) {
    }

    @Override
    public Operation up(MigrationContext context) {
        return CreateTable.builder("people", factory -> new Object() {
                    ColumnBuilder id = factory.newInteger();
                    ColumnBuilder email = factory.newText().nullable(false);
                })
                .primaryKey(table -> table.id)
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropTable.builder("people").build();
    }
}
