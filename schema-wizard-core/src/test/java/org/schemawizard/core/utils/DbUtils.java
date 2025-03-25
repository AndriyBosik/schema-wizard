package org.schemawizard.core.utils;

import org.schemawizard.core.starter.SchemaWizard;

public class DbUtils {
    private DbUtils() {
    }

    public static void initHistoryTable() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("none");
        schemaWizard.up();
    }
}
