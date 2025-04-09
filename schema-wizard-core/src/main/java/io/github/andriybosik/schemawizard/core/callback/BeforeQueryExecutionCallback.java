package io.github.andriybosik.schemawizard.core.callback;

import io.github.andriybosik.schemawizard.core.analyzer.MigrationData;

public interface BeforeQueryExecutionCallback extends Callback {
    void handle(MigrationData data);
}
