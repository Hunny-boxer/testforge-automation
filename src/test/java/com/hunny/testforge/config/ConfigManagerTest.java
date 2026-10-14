package com.hunny.testforge.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ConfigManagerTest {

    @Test(groups = {"UNIT", "SMOKE"})
    public void shouldLoadBaseUrlFromConfigurationResource() {
        Properties properties = ConfigManager.loadFromResource(
                ConfigManager.class.getClassLoader()
        );

        Assert.assertEquals(
                properties.getProperty("baseUrl"),
                "https://www.google.com"
        );
    }

    @Test(groups = {"UNIT", "SMOKE"})
    public void shouldLoadBrowserFromConfigurationResource() {
        Properties properties = ConfigManager.loadFromResource(
                ConfigManager.class.getClassLoader()
        );

        Assert.assertEquals(
                properties.getProperty("browser"),
                "chrome"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldPreferSystemPropertyOverEnvironmentAndFile() {
        Properties properties = propertiesWith(
                "baseUrl",
                "https://file.example"
        );

        String result = ConfigManager.resolve(
                "baseUrl",
                " https://system.example ",
                "https://env.example",
                properties
        );

        Assert.assertEquals(result, "https://system.example");
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldUseEnvironmentWhenSystemPropertyIsMissing() {
        Properties properties = propertiesWith("browser", "chrome");

        Assert.assertEquals(
                ConfigManager.resolve(
                        "browser", null, " firefox ", properties
                ),
                "firefox"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldUseEnvironmentWhenSystemPropertyIsBlank() {
        Properties properties = propertiesWith("browser", "chrome");

        Assert.assertEquals(
                ConfigManager.resolve(
                        "browser", "  ", " firefox ", properties
                ),
                "firefox"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldUseFileDefaultWhenOverridesAreMissing() {
        Properties properties = propertiesWith(
                "baseUrl",
                "https://file.example"
        );

        Assert.assertEquals(
                ConfigManager.resolve(
                        "baseUrl", null, null, properties
                ),
                "https://file.example"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldUseFileDefaultWhenOverridesAreBlank() {
        Properties properties = propertiesWith(
                "baseUrl",
                "https://file.example"
        );

        Assert.assertEquals(
                ConfigManager.resolve(
                        "baseUrl", "  ", "\t", properties
                ),
                "https://file.example"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldReturnNullForNullAndBlankKeys() {
        Assert.assertNull(ConfigManager.get(null));
        Assert.assertNull(ConfigManager.get("  "));

        Properties properties = propertiesWith(
                "baseUrl",
                "https://file.example"
        );

        Assert.assertNull(
                ConfigManager.resolve(
                        null, "system", "environment", properties
                )
        );

        Assert.assertNull(
                ConfigManager.resolve(
                        "  ", "system", "environment", properties
                )
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldReturnNullForUnknownConfigurationKey() {
        Assert.assertNull(
                ConfigManager.resolve(
                        "unknown.configuration.key",
                        null,
                        null,
                        new Properties()
                )
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldReturnNullWhenPropertiesObjectIsNull() {
        Assert.assertNull(
                ConfigManager.resolve(
                        "baseUrl", null, null, null
                )
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldConvertCamelCaseKeysToEnvironmentVariables() {
        Assert.assertEquals(
                ConfigManager.toEnvironmentVariable("apiBaseUrl"),
                "TESTFORGE_API_BASE_URL"
        );

        Assert.assertEquals(
                ConfigManager.toEnvironmentVariable("baseUrl"),
                "TESTFORGE_BASE_URL"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldRejectMissingConfigurationStream() {
        RuntimeException exception = Assert.expectThrows(
                RuntimeException.class,
                () -> ConfigManager.loadProperties(null)
        );

        Assert.assertEquals(
                exception.getMessage(),
                "config.properties not found"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldLoadPropertiesFromProvidedStream()
            throws IOException {

        String content =
                "baseUrl=https://example.test\n"
                + "browser=firefox\n";

        ByteArrayInputStream input = new ByteArrayInputStream(
                content.getBytes(StandardCharsets.ISO_8859_1)
        );

        Properties loadedProperties =
                ConfigManager.loadProperties(input);

        Assert.assertEquals(
                loadedProperties.getProperty("baseUrl"),
                "https://example.test"
        );

        Assert.assertEquals(
                loadedProperties.getProperty("browser"),
                "firefox"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldWrapResourceLookupFailure() {
        ClassLoader failingClassLoader = new ClassLoader() {
            @Override
            public InputStream getResourceAsStream(String name) {
                throw new IllegalStateException(
                        "simulated resource lookup failure"
                );
            }
        };

        RuntimeException exception = Assert.expectThrows(
                RuntimeException.class,
                () -> ConfigManager.loadFromResource(failingClassLoader)
        );

        Assert.assertEquals(
                exception.getMessage(),
                "Failed to load configuration"
        );

        Assert.assertTrue(
                exception.getCause() instanceof IllegalStateException
        );

        Assert.assertEquals(
                exception.getCause().getMessage(),
                "simulated resource lookup failure"
        );
    }

    private Properties propertiesWith(String key, String value) {
        Properties properties = new Properties();
        properties.setProperty(key, value);
        return properties;
    }
}
