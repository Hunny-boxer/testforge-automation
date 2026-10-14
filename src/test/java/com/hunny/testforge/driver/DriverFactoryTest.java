package com.hunny.testforge.driver;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class DriverFactoryTest {

    @AfterMethod(alwaysRun = true)
    public void cleanUpDriver() {
        DriverFactory.quitDriver();
    }

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
    public void shouldUseConfiguredBrowserWhenNoArgumentIsProvided() {
        String originalBrowser = System.getProperty("browser");

        try {
            System.setProperty("browser", "firefox");

            IllegalArgumentException exception = Assert.expectThrows(
                    IllegalArgumentException.class,
                    DriverFactory::initializeDriver
            );

            Assert.assertEquals(
                    exception.getMessage(),
                    "Unsupported browser: firefox",
                    "The no-argument method should use the configured browser"
            );
        } finally {
            if (originalBrowser == null) {
                System.clearProperty("browser");
            } else {
                System.setProperty("browser", originalBrowser);
            }
        }
    }

    @Test(groups = {"UNIT", "SMOKE"})
    public void shouldInitializeChromeDriverSuccessfully() {
        DriverFactory.initializeDriver("CHROME");

        WebDriver driver = DriverFactory.getDriver();

        Assert.assertNotNull(
                driver,
                "Chrome driver should be initialized"
        );

        Assert.assertTrue(
                driver.getWindowHandle() != null
                        && !driver.getWindowHandle().isEmpty(),
                "Chrome should have an active browser window"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldReturnNullWhenDriverIsNotInitialized() {
        Assert.assertNull(
                DriverFactory.getDriver(),
                "Driver should be null when not initialized"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldQuitInitializedDriverSuccessfully() {
        DriverFactory.initializeDriver("chrome");

        Assert.assertNotNull(DriverFactory.getDriver());

        DriverFactory.quitDriver();

        Assert.assertNull(
                DriverFactory.getDriver(),
                "Driver should be null after cleanup"
        );
    }
}
