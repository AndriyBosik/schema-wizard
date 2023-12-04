package org.schemawizard.core.callback;

import org.schemawizard.core.analyzer.MigrationData;

public interface AfterQueryExecutionCallback extends Callback {
    void handle(MigrationData data);
}
