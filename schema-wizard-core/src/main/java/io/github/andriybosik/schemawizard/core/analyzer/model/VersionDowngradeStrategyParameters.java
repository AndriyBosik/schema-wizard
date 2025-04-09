package io.github.andriybosik.schemawizard.core.analyzer.model;

public class VersionDowngradeStrategyParameters extends DowngradeStrategyParameters {
    private final int version;

    public VersionDowngradeStrategyParameters(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }
}
