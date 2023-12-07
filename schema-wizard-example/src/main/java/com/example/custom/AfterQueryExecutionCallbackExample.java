package com.example.custom;

import org.schemawizard.core.analyzer.MigrationData;
import org.schemawizard.core.callback.AfterQueryExecutionCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AfterQueryExecutionCallbackExample implements AfterQueryExecutionCallback {
    private final Logger log = LoggerFactory.getLogger(AfterQueryExecutionCallbackExample.class);

    @Override
    public void handle(MigrationData data) {
        log.info(String.valueOf(data.getVersion()));
        log.info(data.getDescription());
    }
}
