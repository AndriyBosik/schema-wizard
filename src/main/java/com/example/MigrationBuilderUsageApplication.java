package com.example;

import com.example.builder.model.MigrationContext;
import com.example.builder.operation.Operation;
import com.example.migration.UsersTableMigration;

public class MigrationBuilderUsageApplication {
    public static void main(String[] args) {
        UsersTableMigration migration = new UsersTableMigration();
        Operation upOperation = migration.up(new MigrationContext());
        Operation downOperation = migration.down(new MigrationContext());
        System.out.println();
    }
}
