package com.example;

import io.github.andriybosik.schemawizard.core.starter.SchemaWizard;
import io.github.andriybosik.schemawizard.core.starter.SchemaWizardBuilder;

public class MigrationUsageApplication {
    public static void main(String[] args) {
        SchemaWizard schemaWizard = SchemaWizardBuilder.init().build();
        schemaWizard.up();
    }
}
