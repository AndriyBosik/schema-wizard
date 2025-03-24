package org.schemawizard.core.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class TestIOUtils {
    private TestIOUtils() {}

    public static File getResourceFile(String path) {
        ClassLoader loader = TestIOUtils.class.getClassLoader();
        URL url = loader.getResource(path);
        if (url == null) {
            throw new IllegalArgumentException("Path does not exists: " + path);
        }
        return new File(url.getFile());
    }

    public static String getContent(File file) {
        try {
            return Files.readAllLines(file.toPath()).stream()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
