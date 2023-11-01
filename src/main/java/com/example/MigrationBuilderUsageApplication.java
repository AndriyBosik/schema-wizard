package com.example;

import com.example.migration.Migration;
import com.example.migration.model.MigrationContext;
import com.example.migration.operation.Operation;
import com.example.usage.SW002CreatePostsTable;

public class MigrationBuilderUsageApplication {
    public static void main(String[] args) {
        Migration migration = new SW002CreatePostsTable();
        Operation upOperation = migration.up(new MigrationContext());
        Operation downOperation = migration.down(new MigrationContext());
        System.out.println();
    }
}
