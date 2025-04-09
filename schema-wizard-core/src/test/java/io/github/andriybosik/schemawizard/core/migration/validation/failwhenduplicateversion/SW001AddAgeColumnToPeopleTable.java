package io.github.andriybosik.schemawizard.core.migration.validation.failwhenduplicateversion;

import io.github.andriybosik.schemawizard.core.migration.Migration;
import io.github.andriybosik.schemawizard.core.migration.annotation.SWName;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.AddColumns;
import io.github.andriybosik.schemawizard.core.migration.builder.operation.DropColumns;
import io.github.andriybosik.schemawizard.core.migration.model.MigrationContext;
import io.github.andriybosik.schemawizard.core.migration.operation.Operation;

import java.util.List;

@SWName("Add age column")
public class SW001AddAgeColumnToPeopleTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return AddColumns.builder(
                        "people",
                        factory -> List.of(
                                factory.newInteger("age").nullable(false)))
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropColumns.builder("people")
                .columns("age")
                .build();
    }
}
