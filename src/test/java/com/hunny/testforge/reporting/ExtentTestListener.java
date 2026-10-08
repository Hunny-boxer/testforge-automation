package com.hunny.testforge.reporting;

import com.hunny.testforge.driver.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentTestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {

        String packageName = result.getTestClass()
                .getRealClass()
                .getPackageName();

        String category = packageName.contains(".api")
                ? "API"
                : "UI";

        ExtentReportManager.createTest(
                result.getMethod().getMethodName(),
                category
        );
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {

        ExtentReportManager.getTest().fail(result.getThrowable());

        try {

            TakesScreenshot screenshot =
                    (TakesScreenshot) DriverFactory.getDriver();

            String screenshotBase64 =
                    screenshot.getScreenshotAs(OutputType.BASE64);

            ExtentReportManager.getTest().addScreenCaptureFromBase64String(
                    screenshotBase64,
                    "Failure Screenshot"
            );

        } catch (Exception e) {

            ExtentReportManager.getTest().warning(
                    "Could not capture failure screenshot: "
                            + e.getMessage()
            );
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().skip("Test Skipped");
    }
}
