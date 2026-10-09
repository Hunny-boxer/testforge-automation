
package com.hunny.testforge.reporting;

import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ExtentTestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {

        String className =
                result.getTestClass()
                        .getRealClass()
                        .getSimpleName();

        String methodName =
                result.getMethod().getMethodName();

        String testName = className + "." + methodName;

        String[] groups = result.getMethod().getGroups();

        String category = Arrays.stream(groups)
                .filter(group -> group != null && !group.isBlank())
                .collect(Collectors.joining(", "));

        if (category.isBlank()) {
            category = "General";
        }

        ExtentReportManager.createTest(testName, category);
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest()
                    .pass("Test Passed");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {

        if (ExtentReportManager.getTest() != null) {

            if (result.getThrowable() != null) {
                ExtentReportManager.getTest()
                        .fail(result.getThrowable());
            } else {
                ExtentReportManager.getTest()
                        .fail("Test Failed");
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest()
                    .skip("Test Skipped");
        }
    }
}
