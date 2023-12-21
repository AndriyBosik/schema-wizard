package com.example.extension;

import org.schemawizard.core.callback.QueryGeneratedCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryCallback implements QueryGeneratedCallback {
    private final Logger log = LoggerFactory.getLogger(QueryCallback.class);

    @Override
    public void handle(String sql) {
        log.info("Generated SQL: {}", sql);
    }
}
