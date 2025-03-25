package org.schemawizard.core.config.impl;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.schemawizard.core.utils.TestRunnerUtils;

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
