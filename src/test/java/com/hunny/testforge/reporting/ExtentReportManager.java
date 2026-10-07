package com.hunny.testforge.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static void initReports() {

        ExtentSparkReporter sparkReporter =
                new ExtentSparkReporter("test-output/ExtentReport.html");

        sparkReporter.config().setDocumentTitle("TestForge Automation Report");
        sparkReporter.config().setReportName("TestForge Automation Test Results");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("Project", "TestForge Automation");
        extent.setSystemInfo("Framework", "Selenium + TestNG");
        extent.setSystemInfo("Language", "Java");
        extent.setSystemInfo("Browser", "Chrome");
    }

    public static void createTest(String testName) {
        test.set(extent.createTest(testName));
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}
