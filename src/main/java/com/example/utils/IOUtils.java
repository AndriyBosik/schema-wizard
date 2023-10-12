package com.example.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class IOUtils {
    private IOUtils() {
    }

    public static InputStream getInputStream(String file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }
}
