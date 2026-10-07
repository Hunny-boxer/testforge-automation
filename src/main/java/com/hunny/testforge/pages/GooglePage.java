package com.hunny.testforge.pages;

import org.openqa.selenium.WebDriver;

public class GooglePage {

    private final WebDriver driver;

    public GooglePage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://www.google.com");
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
