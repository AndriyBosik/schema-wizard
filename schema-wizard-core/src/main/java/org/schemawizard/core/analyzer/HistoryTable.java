package org.schemawizard.core.analyzer;

public interface HistoryTable {
    void createIfNotExists();

    boolean exists();

    void lockForExecution();
}
