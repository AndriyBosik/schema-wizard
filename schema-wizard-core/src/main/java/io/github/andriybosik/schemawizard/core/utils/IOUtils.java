package io.github.andriybosik.schemawizard.core.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    public static byte[] readAllBytes(InputStream inputStream) {
        try {
            return inputStream.readAllBytes();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
