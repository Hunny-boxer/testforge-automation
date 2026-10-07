package com.hunny.testforge.base;

import com.hunny.testforge.reporting.ExtentReportManager;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class ReportingBaseTest {

    @BeforeSuite
    public void startReporting() {
        ExtentReportManager.initReports();
    }

    @AfterSuite
    public void stopReporting() {
        ExtentReportManager.flushReports();
    }
}
