
package com.hunny.testforge.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ConfigManagerTest {

    @Test(groups = {"UNIT", "SMOKE"})
    public void shouldLoadBaseUrlFromConfiguration() {
        Assert.assertEquals(
                ConfigManager.getBaseUrl(),
                "https://www.google.com",
                "Base URL should be loaded from config.properties"
        );
    }

    @Test(groups = {"UNIT", "SMOKE"})
    public void shouldLoadBrowserFromConfiguration() {
        Assert.assertEquals(
                ConfigManager.getBrowser(),
                "chrome",
                "Browser should be loaded from config.properties"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldReturnNullForUnknownConfigurationKey() {
        Assert.assertNull(
                ConfigManager.get("unknown.configuration.key"),
                "Unknown configuration keys should return null"
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
}
