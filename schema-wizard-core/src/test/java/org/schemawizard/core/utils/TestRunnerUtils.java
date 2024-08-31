package org.schemawizard.core.utils;

import org.schemawizard.core.config.TestRunner;
import org.schemawizard.core.config.impl.LocalTestRunner;
import org.schemawizard.core.config.impl.PipelineTestRunner;

import java.util.Map;

public class TestRunnerUtils {
    private static final String TEST_ENV = "TEST_ENV";
    private static final String LOCAL_ENV_VALUE = "local";
    private static final String PIPELINE_ENV_VALUE = "pipeline";

    private static final String ENV_VALUE = getEnvValue();

    public static final Map<String, TestRunner> envToRunner = Map.of(
            LOCAL_ENV_VALUE, new LocalTestRunner(),
            PIPELINE_ENV_VALUE, new PipelineTestRunner()
    );

    public static TestRunner getRunner() {
        return envToRunner.get(ENV_VALUE);
    }

    private static String getEnvValue() {
        String testEnv = System.getenv(TEST_ENV);
        if (LOCAL_ENV_VALUE.equalsIgnoreCase(testEnv)) {
            return LOCAL_ENV_VALUE;
        }
        return PIPELINE_ENV_VALUE;
    }
}
