package org.schemawizard.core.analyzer.model;

public class ContextDowngradeStrategyParameters extends DowngradeStrategyParameters {
    private final String context;

    public ContextDowngradeStrategyParameters(String context) {
        this.context = context;
    }

    public String getContext() {
        return context;
    }
}
