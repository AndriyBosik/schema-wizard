package org.schemawizard.core.analyzer.model;

public class CountDowngradeStrategyParameters extends DowngradeStrategyParameters {
    private final int count;

    public CountDowngradeStrategyParameters(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}