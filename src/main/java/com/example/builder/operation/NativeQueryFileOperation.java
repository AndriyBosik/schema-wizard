package com.example.builder.operation;

public class NativeQueryFileOperation implements Operation {
    private final String filePath;

    public NativeQueryFileOperation(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
