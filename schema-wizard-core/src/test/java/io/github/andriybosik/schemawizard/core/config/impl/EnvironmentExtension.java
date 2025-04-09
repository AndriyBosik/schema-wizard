package io.github.andriybosik.schemawizard.core.config.impl;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import io.github.andriybosik.schemawizard.core.utils.TestRunnerUtils;

public class EnvironmentExtension implements BeforeAllCallback, AfterAllCallback {
    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        TestRunnerUtils.getRunner().configureEnvironment();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        TestRunnerUtils.getRunner().shutdownEnvironment();
    }
}
