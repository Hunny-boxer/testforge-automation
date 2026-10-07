package com.hunny.testforge.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GoogleSearchTest {

    @Test
    public void openGoogle() {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get("https://www.google.com");

            String title = driver.getTitle();

            System.out.println("Page title: " + title);

            Assert.assertTrue(
                    title != null && !title.isEmpty(),
                    "Page title should not be empty"
            );

        } finally {
            driver.quit();
        }
    }
}
