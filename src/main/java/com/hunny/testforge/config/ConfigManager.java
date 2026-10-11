
package com.hunny.testforge.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = loadFromResource();

    private ConfigManager() {
        // Utility class; prevent unnecessary instances.
    }

    private static Properties loadFromResource() {
        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            return loadProperties(input);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    static Properties loadProperties(InputStream input)
            throws IOException {

        if (input == null) {
            throw new RuntimeException("config.properties not found");
        }

        Properties loadedProperties = new Properties();
        loadedProperties.load(input);

        return loadedProperties;
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return get("baseUrl");
    }

    public static String getBrowser() {
        return get("browser");
    }
}
