package com.hunny.testforge.utils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TestDataReader {

    private TestDataReader() {
        // Utility class
    }

    public static String readJsonFile(String filePath) {

        try (InputStream inputStream =
                     TestDataReader.class
                             .getClassLoader()
                             .getResourceAsStream(filePath)) {

            if (inputStream == null) {
                throw new RuntimeException(
                        "Test data file not found: " + filePath
                );
            }

            return new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to read test data file: " + filePath,
                    e
            );
        }
    }
}
