package io.github.andriybosik.schemawizard.core.config.impl;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import io.github.andriybosik.schemawizard.core.starter.SchemaWizard;
import io.github.andriybosik.schemawizard.core.utils.FactoryUtils;

public class MigrationHistoryExtension implements BeforeAllCallback {
    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("none");
        schemaWizard.up();
    }
}
