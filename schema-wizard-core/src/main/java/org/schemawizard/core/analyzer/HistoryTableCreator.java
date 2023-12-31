package org.schemawizard.core.analyzer;

public interface HistoryTableCreator {
    void createTableIfNotExist();

    boolean historyTableExists();
}
