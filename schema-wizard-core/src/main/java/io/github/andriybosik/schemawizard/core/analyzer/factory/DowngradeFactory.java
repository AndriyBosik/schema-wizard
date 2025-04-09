package io.github.andriybosik.schemawizard.core.analyzer.factory;

import io.github.andriybosik.schemawizard.core.analyzer.model.DowngradeStrategyParameters;
import io.github.andriybosik.schemawizard.core.analyzer.service.DowngradeStrategy;

public interface DowngradeFactory {
    DowngradeStrategy getInstance(DowngradeStrategyParameters parameters);
}
