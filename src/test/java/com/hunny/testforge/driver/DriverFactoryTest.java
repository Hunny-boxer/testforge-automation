
package com.hunny.testforge.driver;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DriverFactoryTest {

    @Test(groups = {"UNIT", "SMOKE"})
    public void shouldRejectUnsupportedBrowser() {
        IllegalArgumentException exception = Assert.expectThrows(
                IllegalArgumentException.class,
                () -> DriverFactory.initializeDriver("firefox")
        );

        Assert.assertEquals(
                exception.getMessage(),
                "Unsupported browser: firefox"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldReturnNullWhenDriverIsNotInitialized() {
        DriverFactory.quitDriver();

        Assert.assertNull(
                DriverFactory.getDriver(),
                "Driver should be null when not initialized"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldAllowCleanupWhenDriverIsNotInitialized() {
        DriverFactory.quitDriver();

        Assert.assertNull(
                DriverFactory.getDriver(),
                "Cleanup should leave the driver null"
        );
    }
}
