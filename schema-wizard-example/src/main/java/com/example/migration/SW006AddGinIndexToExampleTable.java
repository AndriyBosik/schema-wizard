package com.example.migration;

import com.example.custom.AddHashIndexOperation;
import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.builder.operation.NativeQuery;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

public class SW006AddGinIndexToExampleTable implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return new AddHashIndexOperation("public", "example", "idx_example_title_hash", new String[]{"title"});
    }

    @Override
    public Operation down(MigrationContext context) {
        return NativeQuery.builder()
                .sql("DROP INDEX IF EXISTS idx_example_title_gin")
                .build();
    }
}
