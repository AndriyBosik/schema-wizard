package com.example.callback;

public interface BeforeQueryExecutionCallback {
    // TODO `MigrationInfo info` instead of `String sql`???
    // TODO Pass Connection instance as method parameter or force developer to use dependency injection in order to inject ConnectionHolder???
    void apply(String sql);
}
