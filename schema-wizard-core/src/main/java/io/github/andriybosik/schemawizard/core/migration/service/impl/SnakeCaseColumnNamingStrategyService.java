package io.github.andriybosik.schemawizard.core.migration.service.impl;

import io.github.andriybosik.schemawizard.core.migration.service.ColumnNamingStrategyService;
import io.github.andriybosik.schemawizard.core.utils.StringUtils;

public class SnakeCaseColumnNamingStrategyService implements ColumnNamingStrategyService {
    @Override
    public String map(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        StringBuilder snakeCaseName = new StringBuilder();
        char[] symbols = name.strip().toCharArray();
        for (char symbol : symbols) {
            if (!Character.isUpperCase(symbol)) {
                snakeCaseName.append(symbol);
                continue;
            }
            if (snakeCaseName.length() > 0) {
                snakeCaseName.append('_');
            }
            snakeCaseName.append(Character.toLowerCase(symbol));
        }
        return snakeCaseName.toString();
    }
}
