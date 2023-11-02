package com.example.callback.impl;

import com.example.callback.AfterQueryExecutionCallback;

public class LoggingCallback implements AfterQueryExecutionCallback {
    @Override
    public void apply(String sql) {
        // TODO log SQL
    }
}
