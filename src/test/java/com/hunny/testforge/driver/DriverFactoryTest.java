package com.hunny.testforge.driver;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

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
    public void shouldCloseExistingDriverBeforeReplacingIt() {
        AtomicInteger quitCalls = new AtomicInteger();
        WebDriver firstDriver = fakeDriver(quitCalls, false);
        WebDriver replacementDriver =
                fakeDriver(new AtomicInteger(), false);

        DriverFactory.initializeDriver("chrome", () -> firstDriver);
        Assert.assertSame(DriverFactory.getDriver(), firstDriver);

        DriverFactory.initializeDriver("CHROME", () -> {
            Assert.assertEquals(
                    quitCalls.get(),
                    1,
                    "Existing driver should be closed before replacement"
            );
            Assert.assertNull(
                    DriverFactory.getDriver(),
                    "Old driver reference should be cleared first"
            );
            return replacementDriver;
        });

        Assert.assertSame(
                DriverFactory.getDriver(),
                replacementDriver
        );
        Assert.assertEquals(quitCalls.get(), 1);
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldClearDriverWhenReplacementCreationFails() {
        AtomicInteger quitCalls = new AtomicInteger();
        WebDriver existingDriver = fakeDriver(quitCalls, false);

        DriverFactory.initializeDriver("chrome", () -> existingDriver);

        IllegalStateException exception = Assert.expectThrows(
                IllegalStateException.class,
                () -> DriverFactory.initializeDriver("chrome", () -> {
                    throw new IllegalStateException(
                            "simulated driver creation failure"
                    );
                })
        );

        Assert.assertEquals(
                exception.getMessage(),
                "simulated driver creation failure"
        );
        Assert.assertEquals(quitCalls.get(), 1);
        Assert.assertNull(DriverFactory.getDriver());
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldRejectNullDriverSupplierResult() {
        IllegalStateException exception = Assert.expectThrows(
                IllegalStateException.class,
                () -> DriverFactory.initializeDriver("chrome", () -> null)
        );

        Assert.assertEquals(
                exception.getMessage(),
                "Driver supplier returned null"
        );
        Assert.assertNull(DriverFactory.getDriver());
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldClearDriverReferenceWhenQuitFails() {
        WebDriver faultyDriver = fakeDriver(
                new AtomicInteger(),
                true
        );

        DriverFactory.initializeDriver("chrome", () -> faultyDriver);

        Assert.expectThrows(
                IllegalStateException.class,
                DriverFactory::quitDriver
        );

        Assert.assertNull(
                DriverFactory.getDriver(),
                "Driver reference should be cleared even when quit fails"
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
                    "Unsupported browser: firefox"
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
        Assert.assertNull(DriverFactory.getDriver());
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

    private WebDriver fakeDriver(
            AtomicInteger quitCalls,
            boolean failOnQuit) {

        return (WebDriver) Proxy.newProxyInstance(
                WebDriver.class.getClassLoader(),
                new Class<?>[]{WebDriver.class},
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "quit":
                            quitCalls.incrementAndGet();

                            if (failOnQuit) {
                                throw new IllegalStateException(
                                        "simulated quit failure"
                                );
                            }
                            return null;

                        case "getWindowHandle":
                            return "fake-window";

                        case "toString":
                            return "FakeWebDriver";

                        case "hashCode":
                            return System.identityHashCode(proxy);

                        case "equals":
                            return proxy == args[0];

                        default:
                            return null;
                    }
                }
        );
    }
}
