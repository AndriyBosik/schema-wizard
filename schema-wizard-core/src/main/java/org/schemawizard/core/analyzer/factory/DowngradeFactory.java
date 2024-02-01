package org.schemawizard.core.analyzer.factory;

import org.schemawizard.core.analyzer.model.DowngradeStrategyParameters;
import org.schemawizard.core.analyzer.service.DowngradeStrategy;

public interface DowngradeFactory {
    DowngradeStrategy getInstance(DowngradeStrategyParameters parameters);
}
