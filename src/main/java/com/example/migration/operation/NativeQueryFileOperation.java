package com.example.migration.operation;

public class NativeQueryFileOperation implements Operation {
    private final String filePath;

    public NativeQueryFileOperation(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
