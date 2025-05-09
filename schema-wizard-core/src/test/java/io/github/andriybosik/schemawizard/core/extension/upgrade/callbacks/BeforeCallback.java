package io.github.andriybosik.schemawizard.core.extension.upgrade.callbacks;

import io.github.andriybosik.schemawizard.core.analyzer.MigrationData;
import io.github.andriybosik.schemawizard.core.callback.BeforeQueryExecutionCallback;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class BeforeCallback implements BeforeQueryExecutionCallback {
    private static List<ZonedDateTime> executions;

    public BeforeCallback() {
        executions = new ArrayList<>();
    }

    @Override
    public void handle(MigrationData data) {
        executions.add(ZonedDateTime.now());
    }

    public static List<ZonedDateTime> getExecutions() {
        return executions;
    }
}
