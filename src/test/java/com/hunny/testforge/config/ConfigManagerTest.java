
package com.hunny.testforge.config;

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
}
