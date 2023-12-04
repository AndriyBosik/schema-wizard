package org.schemawizard.core.callback.impl;

import org.schemawizard.core.callback.QueryGeneratedCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryLoggerCallback implements QueryGeneratedCallback {
    private final Logger log = LoggerFactory.getLogger(QueryLoggerCallback.class);

    @Override
    public void handle(String sql) {
        log.info(sql);
    }
}
