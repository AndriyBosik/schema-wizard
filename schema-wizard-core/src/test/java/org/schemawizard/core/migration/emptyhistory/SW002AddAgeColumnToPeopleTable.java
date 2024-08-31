package org.schemawizard.core.migration.emptyhistory;

import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.annotation.SWName;
import org.schemawizard.core.migration.builder.operation.AddColumns;
import org.schemawizard.core.migration.builder.operation.DropColumns;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

import java.util.List;

@SWName("Add age column")
public class SW002AddAgeColumnToPeopleTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return AddColumns.builder(
                        "people",
                        factory -> List.of(
                                factory.integer("age").nullable(false)))
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropColumns.builder("people")
                .columns("age")
                .build();
    }
}
