package org.schemawizard.core.config.impl;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.schemawizard.core.starter.SchemaWizard;
import org.schemawizard.core.utils.FactoryUtils;

public class MigrationHistoryExtension implements BeforeAllCallback {
    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("none");
        schemaWizard.up();
    }
}
