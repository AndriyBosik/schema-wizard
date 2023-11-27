package org.schemawizard.core.migration.operation;

public class NativeQueryFileOperation implements Operation {
    private final String filePath;

    public NativeQueryFileOperation(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
