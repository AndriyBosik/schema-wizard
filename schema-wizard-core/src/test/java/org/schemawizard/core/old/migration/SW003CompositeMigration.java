package org.schemawizard.core.old.migration;

import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.builder.operation.AddColumns;
import org.schemawizard.core.migration.builder.operation.Composite;
import org.schemawizard.core.migration.builder.operation.DropColumns;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;

import java.util.List;

public class SW003CompositeMigration implements Migration {
    @Override
    public Operation up(MigrationContext context) {
        return Composite.builder()
                .add(AddColumns.builder("SCHEMAWIZARD", "users", factory -> List.of(
                        factory.newText("column1")
                )).build())
                .add(AddColumns.builder("SCHEMAWIZARD", "users", factory -> List.of(
                        factory.newText("column2")
                )).build())
                .add(AddColumns.builder("SCHEMAWIZARD", "users", factory -> List.of(
                        factory.newText("column3")
                )).build())
                .build();
    }

    @Override
    public Operation down(MigrationContext context) {
        return DropColumns.builder("SCHEMAWIZARD", "users")
                .columns("column1", "column2", "column3")
                .build();
    }
}
