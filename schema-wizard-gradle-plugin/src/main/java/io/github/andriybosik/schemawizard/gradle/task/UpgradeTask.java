package io.github.andriybosik.schemawizard.gradle.task;

import io.github.andriybosik.schemawizard.core.starter.SchemaWizard;

public class UpgradeTask extends AbstractSchemaWizardTask {
    @Override
    protected Object doExecute(SchemaWizard schemaWizard) {
        schemaWizard.up();
        return null;
    }
}
