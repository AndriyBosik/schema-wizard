package io.github.andriybosik.schemawizard.core.extension.upgrade.callbacks;

import io.github.andriybosik.schemawizard.core.analyzer.MigrationData;
import io.github.andriybosik.schemawizard.core.callback.AfterQueryExecutionCallback;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class AfterCallback implements AfterQueryExecutionCallback {
    private static List<ZonedDateTime> executions;

    public AfterCallback() {
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
