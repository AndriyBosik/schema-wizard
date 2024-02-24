package com.example.extension.callback.after;

import org.schemawizard.core.analyzer.MigrationData;
import org.schemawizard.core.callback.AfterQueryExecutionCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AfterCallback implements AfterQueryExecutionCallback {
    private final Logger log = LoggerFactory.getLogger(AfterCallback.class);

    @Override
    public void handle(MigrationData data) {
        log.info("Executed: {}; Description: {}", data.getVersion(), data.getDescription());
    }
}
