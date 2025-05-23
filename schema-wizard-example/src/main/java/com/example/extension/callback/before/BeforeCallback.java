package com.example.extension.callback.before;

import io.github.andriybosik.schemawizard.core.analyzer.MigrationData;
import io.github.andriybosik.schemawizard.core.callback.BeforeQueryExecutionCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeforeCallback implements BeforeQueryExecutionCallback {
    private final Logger log = LoggerFactory.getLogger(BeforeCallback.class);
    @Override
    public void handle(MigrationData data) {
        log.info("Running: {}; Description: {}", data.getVersion(), data.getDescription());
    }
}
