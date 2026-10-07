package com.hunny.testforge.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class GoogleSearchTest {

    @Test
    public void openGoogle() {

        WebDriver driver = new ChromeDriver();

        driver.get("https://www.google.com");

        System.out.println("Page title: " + driver.getTitle());

        driver.quit();
    }
}
