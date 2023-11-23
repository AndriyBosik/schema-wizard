package org.schemawizard.core;

import org.schemawizard.core.migration.Migration;
import org.schemawizard.core.migration.model.MigrationContext;
import org.schemawizard.core.migration.operation.Operation;
import org.schemawizard.core.usage.SW002CreatePostsTable;

public class MigrationBuilderUsageApplication {
    public static void main(String[] args) {
        Migration migration = new SW002CreatePostsTable();
        Operation upOperation = migration.up(new MigrationContext());
        Operation downOperation = migration.down(new MigrationContext());
        System.out.println();
    }
}
