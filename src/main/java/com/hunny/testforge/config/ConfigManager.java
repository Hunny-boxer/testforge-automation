
package com.hunny.testforge.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public final class ConfigManager {

    private static final Properties PROPERTIES =
            loadFromResource(ConfigManager.class.getClassLoader());

    private ConfigManager() {
        // Utility class
    }

    static Properties loadFromResource(ClassLoader classLoader) {
        try (InputStream input =
                     classLoader.getResourceAsStream("config.properties")) {

            return loadProperties(input);

        } catch (Exception exception) {
            throw new RuntimeException(
                    "Failed to load configuration",
                    exception
            );
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

    /**
     * Configuration priority:
     * 1. Java system property
     * 2. Environment variable
     * 3. config.properties
     */
    public static String get(String key) {
        if (key == null || key.isBlank()) {
            return null;
        }

        String systemValue = System.getProperty(key);

        if (isConfigured(systemValue)) {
            return systemValue.trim();
        }

        String environmentValue =
                System.getenv(toEnvironmentVariable(key));

        if (isConfigured(environmentValue)) {
            return environmentValue.trim();
        }

        return PROPERTIES.getProperty(key);
    }

    public static String getBaseUrl() {
        return get("baseUrl");
    }

    public static String getBrowser() {
        return get("browser");
    }

    public static String getApiBaseUrl() {
        return get("apiBaseUrl");
    }

    /**
     * Optional because local runs and external pull requests may
     * not have an API token configured.
     */
    public static String getApiToken() {
        return get("apiToken");
    }

    private static boolean isConfigured(String value) {
        return value != null && !value.isBlank();
    }

    private static String toEnvironmentVariable(String key) {
        String normalizedKey = key.replaceAll(
                "([a-z0-9])([A-Z])",
                "$1_$2"
        );

        return "TESTFORGE_"
                + normalizedKey.toUpperCase(Locale.ROOT);
    }
}
