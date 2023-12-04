package org.schemawizard.core.callback;

import org.schemawizard.core.analyzer.MigrationData;

public interface BeforeQueryExecutionCallback extends Callback {
    void handle(MigrationData data);
}
