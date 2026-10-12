
package com.hunny.testforge.api;

import com.aventstack.extentreports.ExtentTest;
import com.hunny.testforge.config.ConfigManager;
import com.hunny.testforge.reporting.ExtentReportManager;
import com.hunny.testforge.reporting.ExtentTestListener;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class ApiValidationNegativeTest {

    @Test(groups = {"API", "REGRESSION", "NEGATIVE", "SECURITY"})
    public void malformedJsonShouldReturn400() {

        Response response =
                given()
                        .spec(authorizedRequest())
                        .contentType("application/json")
                        .body("{\"name\":")
                        .when()
                        .post();

        logResult("Malformed JSON", response);

        Assert.assertEquals(
                response.getStatusCode(),
                400,
                "Malformed JSON should return HTTP 400"
        );
    }

    @Test(groups = {"API", "REGRESSION", "NEGATIVE", "SECURITY"})
    public void missingRequiredNameShouldReturn422() {

        String requestBody =
                "{"
                + "\"email\":\"testforge-missing-name@example.com\","
                + "\"gender\":\"male\","
                + "\"status\":\"active\""
                + "}";

        Response response =
                given()
                        .spec(authorizedRequest())
                        .contentType("application/json")
                        .body(requestBody)
                        .when()
                        .post();

        logResult("Missing required name", response);

        Assert.assertEquals(
                response.getStatusCode(),
                422,
                "Missing required name should return HTTP 422"
        );
    }

    @Test(groups = {"API", "REGRESSION", "NEGATIVE", "SECURITY"})
    public void unsupportedContentTypeShouldReturn415() {

        String requestBody =
                "{"
                + "\"name\":\"TestForge QA\","
                + "\"email\":\"testforge-content-type@example.com\","
                + "\"gender\":\"male\","
                + "\"status\":\"active\""
                + "}";

        Response response =
                given()
                        .spec(authorizedRequest())
                        .contentType("text/plain")
                        .body(requestBody)
                        .when()
                        .post();

        logResult("Unsupported Content-Type", response);

        Assert.assertEquals(
                response.getStatusCode(),
                415,
                "Unsupported media type should return HTTP 415"
        );
    }

    @Test(groups = {"API", "REGRESSION", "NEGATIVE", "SECURITY"})
    public void invalidGenderShouldReturn422() {

        String requestBody =
                "{"
                + "\"name\":\"TestForge QA\","
                + "\"email\":\"testforge-invalid-gender@example.com\","
                + "\"gender\":\"unknown\","
                + "\"status\":\"active\""
                + "}";

        Response response =
                given()
                        .spec(authorizedRequest())
                        .contentType("application/json")
                        .body(requestBody)
                        .when()
                        .post();

        logResult("Invalid gender value", response);

        Assert.assertEquals(
                response.getStatusCode(),
                422,
                "Invalid gender should return HTTP 422"
        );
    }

    private RequestSpecification authorizedRequest() {

        String baseUrl = ConfigManager.getApiBaseUrl();

        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException(
                    "API base URL is not configured."
            );
        }

        String token = ConfigManager.getApiToken();

        if (token == null || token.isBlank()) {
            throw new SkipException(
                    "API validation tests skipped because "
                    + "TESTFORGE_API_TOKEN is not configured."
            );
        }

        return given()
                .config(ApiBaseTest.defaultRestAssuredConfig())
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token);
    }

    private void logResult(
            String scenario,
            Response response) {

        String contentType = response.getHeader("Content-Type");

        if (contentType == null) {
            contentType = "Not provided";
        }

        System.out.println("Scenario: " + scenario);
        System.out.println("HTTP Status: " + response.getStatusCode());
        System.out.println("Response Content-Type: " + contentType);

        ExtentTest extentTest = ExtentReportManager.getTest();

        if (extentTest != null) {
            extentTest.info("Scenario: " + scenario);
            extentTest.info(
                    "HTTP Status: " + response.getStatusCode()
            );
            extentTest.info(
                    "Response Content-Type: " + contentType
            );
        }
    }
}
