package com.hunny.testforge.base;

import com.hunny.testforge.driver.DriverFactory;
import com.hunny.testforge.reporting.ExtentReportManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    protected WebDriver driver;

    @BeforeSuite
    public void startReporting() {
        ExtentReportManager.initReports();
    }

    @BeforeMethod
    public void setUp() {
        DriverFactory.initializeDriver();
        driver = DriverFactory.getDriver();
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    @AfterSuite
    public void stopReporting() {
        ExtentReportManager.flushReports();
    }
}
