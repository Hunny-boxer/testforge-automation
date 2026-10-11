package com.hunny.testforge.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestDataReaderTest {

    @Test(groups = {"UNIT", "SMOKE"})
    public void shouldReadJsonResourceSuccessfully() {
        String json = TestDataReader.readJsonFile(
                "testdata/create-user.json"
        );

        Assert.assertNotNull(
                json,
                "JSON test data should not be null"
        );

        Assert.assertTrue(
                json.contains("\"name\": \"Hunny Boxer\""),
                "JSON should contain the expected user name"
        );

        Assert.assertTrue(
                json.contains("\"username\": \"hunnyboxer\""),
                "JSON should contain the expected username"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldThrowExceptionWhenJsonFileIsMissing() {
        String filePath = "testdata/missing-test-data.json";

        RuntimeException exception = Assert.expectThrows(
                RuntimeException.class,
                () -> TestDataReader.readJsonFile(filePath)
        );

        Assert.assertEquals(
                exception.getMessage(),
                "Unable to read test data file: " + filePath
        );

        Assert.assertNotNull(
                exception.getCause(),
                "The exception should preserve the underlying cause"
        );

        Assert.assertEquals(
                exception.getCause().getMessage(),
                "Test data file not found: " + filePath
        );
    }
}
