package io.github.andriybosik.schemawizard.core.migration.metadata;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;

import java.util.Set;

public enum ReferentialAction {
    NO_ACTION("NO ACTION", DatabaseProvider.POSTGRESQL),
    RESTRICT("RESTRICT", DatabaseProvider.POSTGRESQL),
    CASCADE("CASCADE", DatabaseProvider.POSTGRESQL, DatabaseProvider.ORACLE),
    SET_NULL("SET NULL", DatabaseProvider.POSTGRESQL, DatabaseProvider.ORACLE),
    SET_DEFAULT("SET DEFAULT", DatabaseProvider.POSTGRESQL);

    private final String value;
    private final Set<DatabaseProvider> supportedProviders;

    ReferentialAction(String value, DatabaseProvider... supportedProviders) {
        this.value = value;
        this.supportedProviders = Set.of(supportedProviders);
    }

    public String getValue() {
        return value;
    }

    public Set<DatabaseProvider> getSupportedProviders() {
        return supportedProviders;
    }
}
