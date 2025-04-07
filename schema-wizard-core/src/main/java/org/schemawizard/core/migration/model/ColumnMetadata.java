package org.schemawizard.core.migration.model;

public class ColumnMetadata {
    private final boolean nullAllowed;

    public ColumnMetadata(boolean nullAllowed) {
        this.nullAllowed = nullAllowed;
    }

    public boolean isNullAllowed() {
        return nullAllowed;
    }
}
