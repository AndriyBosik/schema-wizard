package io.github.andriybosik.schemawizard.core.analyzer.service;

import java.sql.PreparedStatement;
import java.util.function.Function;

public interface DowngradeStrategy {
    <T> T apply(Function<PreparedStatement, T> action);

    String getNoMigrationsFoundMessage();
}
