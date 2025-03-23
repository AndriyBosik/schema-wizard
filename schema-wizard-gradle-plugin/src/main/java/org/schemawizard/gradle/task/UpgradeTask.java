package org.schemawizard.gradle.task;

import org.schemawizard.core.starter.SchemaWizard;

public class UpgradeTask extends AbstractSchemaWizardTask {
    @Override
    protected Object doExecute(SchemaWizard schemaWizard) {
        schemaWizard.up();
        return null;
    }
}
