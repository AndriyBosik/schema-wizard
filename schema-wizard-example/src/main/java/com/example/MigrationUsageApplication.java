package com.example;

import com.example.custom.AddHashIndexOperationResolver;
import com.example.custom.AfterQueryExecutionCallbackExample;
import org.schemawizard.core.callback.AfterQueryExecutionCallback;
import org.schemawizard.core.starter.SchemaWizard;
import org.schemawizard.core.starter.SchemaWizardBuilder;

public class MigrationUsageApplication {
    public static void main(String[] args) {
        SchemaWizard schemaWizard = SchemaWizardBuilder.init()
                .registerResolver(AddHashIndexOperationResolver.class)
                .registerCallback(AfterQueryExecutionCallback.class, AfterQueryExecutionCallbackExample.class)
                .build();
        schemaWizard.up();
    }
}
