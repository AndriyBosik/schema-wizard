package org.schemawizard.core.migration.metadata;

public enum ReferentialAction {
    NO_ACTION("NO ACTION"),
    RESTRICT("RESTRICT"),
    CASCADE("CASCADE"),
    SET_NULL("SET NULL"),
    SET_DEFAULT("SET DEFAULT");

    private final String value;

    ReferentialAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
