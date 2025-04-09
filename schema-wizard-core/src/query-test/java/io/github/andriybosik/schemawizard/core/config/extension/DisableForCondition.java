package io.github.andriybosik.schemawizard.core.config.extension;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import io.github.andriybosik.schemawizard.core.config.TestContext;
import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DisableForCondition implements ExecutionCondition {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        Set<DatabaseProvider> providers = context.getTestMethod()
                .filter(method -> method.isAnnotationPresent(DisableFor.class))
                .map(method -> method.getAnnotation(DisableFor.class))
                .map(DisableFor::value)
                .map(Arrays::asList)
                .map(HashSet::new)
                .orElseGet(HashSet::new);

        DatabaseProvider provider = TestContext.getProvider();
        if (providers.contains(provider)) {
            return ConditionEvaluationResult.disabled("Disabled because @DisableFor annotation contains " + provider + " provider");
        }

        return ConditionEvaluationResult.enabled(null);
    }
}
