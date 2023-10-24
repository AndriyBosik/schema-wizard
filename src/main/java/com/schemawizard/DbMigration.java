package com.schemawizard;

public interface DbMigration {
    void up();

    void down();
}
