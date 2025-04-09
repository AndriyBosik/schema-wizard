package io.github.andriybosik.schemawizard.core.utils;

import io.github.andriybosik.schemawizard.core.starter.SchemaWizard;

public class DbUtils {
    private DbUtils() {
    }

    public static void initHistoryTable() {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("none");
        schemaWizard.up();
    }
}
