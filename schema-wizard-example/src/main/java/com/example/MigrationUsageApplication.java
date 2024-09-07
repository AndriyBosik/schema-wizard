package com.example;

import org.schemawizard.core.starter.SchemaWizard;
import org.schemawizard.core.starter.SchemaWizardBuilder;

public class MigrationUsageApplication {
    public static void main(String[] args) throws InterruptedException {
        SchemaWizard schemaWizard = SchemaWizardBuilder.init().build();

        schemaWizard.up();
    }
}
