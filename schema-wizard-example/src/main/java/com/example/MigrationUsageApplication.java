package com.example;

import org.schemawizard.core.starter.SchemaWizard;
import org.schemawizard.core.starter.SchemaWizardBuilder;

import java.sql.SQLException;

public class MigrationUsageApplication {
    public static void main(String[] args) throws InterruptedException {
        SchemaWizard first = SchemaWizardBuilder.init().build();
        SchemaWizard second = SchemaWizardBuilder.init().build();

        Thread firstThread = new Thread(first::up);
        Thread secondThread = new Thread(second::up);

        firstThread.start();
        secondThread.start();

        firstThread.join();
        secondThread.join();
    }
}
