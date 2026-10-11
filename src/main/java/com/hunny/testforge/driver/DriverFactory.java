
package com.hunny.testforge.driver;

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
        if ("chrome".equalsIgnoreCase(browser)) {
            ChromeOptions options = new ChromeOptions();

            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");

            driver = new ChromeDriver(options);
        } else {
            throw new IllegalArgumentException(
                    "Unsupported browser: " + browser
            );
        }
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
