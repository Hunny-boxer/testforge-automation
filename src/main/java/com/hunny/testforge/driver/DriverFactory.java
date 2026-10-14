package com.hunny.testforge.driver;

import java.util.function.Supplier;

import com.hunny.testforge.config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    private static WebDriver driver;

    private DriverFactory() {
        // Utility class; prevent unnecessary instances.
    }

    public static void initializeDriver() {
        initializeDriver(ConfigManager.getBrowser());
    }

    public static void initializeDriver(String browser) {
        initializeDriver(browser, DriverFactory::createChromeDriver);
    }

    // Package-private overload allows lifecycle tests without launching Chrome.
    static void initializeDriver(
            String browser,
            Supplier<WebDriver> driverSupplier) {

        if (!"chrome".equalsIgnoreCase(browser)) {
            throw new IllegalArgumentException(
                    "Unsupported browser: " + browser
            );
        }

        // Close any existing session before replacing it.
        if (driver != null) {
            quitDriver();
        }

        WebDriver newDriver = driverSupplier.get();

        if (newDriver == null) {
            throw new IllegalStateException(
                    "Driver supplier returned null"
            );
        }

        driver = newDriver;
    }

    private static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        return new ChromeDriver(options);
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                driver = null;
            }
        }
    }
}
