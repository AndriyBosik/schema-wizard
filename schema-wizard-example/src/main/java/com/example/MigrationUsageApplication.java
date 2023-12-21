package com.example;

import com.example.extension.BeforeCallback;
import com.example.extension.CreateEnumOperationResolver;
import com.example.extension.QueryCallback;
import org.schemawizard.core.callback.BeforeQueryExecutionCallback;
import org.schemawizard.core.callback.QueryGeneratedCallback;
import org.schemawizard.core.starter.SchemaWizard;
import org.schemawizard.core.starter.SchemaWizardBuilder;

public class MigrationUsageApplication {
    public static void main(String[] args) {
        SchemaWizard schemaWizard = SchemaWizardBuilder.init()
                .registerResolver(CreateEnumOperationResolver.class)
                .registerCallback(BeforeQueryExecutionCallback.class, BeforeCallback.class)
                .registerCallback(QueryGeneratedCallback.class, QueryCallback.class)
                .build();
        schemaWizard.up();
    }
}
