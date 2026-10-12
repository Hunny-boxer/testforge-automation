
package com.hunny.testforge.api;

import com.aventstack.extentreports.ExtentTest;
import com.hunny.testforge.reporting.ExtentReportManager;
import com.hunny.testforge.reporting.ExtentTestListener;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class ApiValidationNegativeTest {

    private static final String BASE_URL =
            "https://gorest.in/public/v2/users";

    private static final String TEST_TOKEN =
            "testforge-demo-token";

    @Test(groups = {"API", "REGRESSION", "NEGATIVE", "SECURITY"})
    public void malformedJsonShouldReturn400() {

        Response response =
                given()
                        .baseUri(BASE_URL)
                        .header(
                                "Authorization",
                                "Bearer " + TEST_TOKEN
                        )
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
                        .baseUri(BASE_URL)
                        .header(
                                "Authorization",
                                "Bearer " + TEST_TOKEN
                        )
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
                        .baseUri(BASE_URL)
                        .header(
                                "Authorization",
                                "Bearer " + TEST_TOKEN
                        )
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
                        .baseUri(BASE_URL)
                        .header(
                                "Authorization",
                                "Bearer " + TEST_TOKEN
                        )
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

    private void logResult(
            String scenario,
            Response response) {

        String responseContentType =
                response.getHeader("Content-Type");

        if (responseContentType == null) {
            responseContentType = "Not provided";
        }

        System.out.println("Scenario: " + scenario);
        System.out.println(
                "HTTP Status: " + response.getStatusCode()
        );
        System.out.println(
                "Response Content-Type: " + responseContentType
        );

        ExtentTest extentTest = ExtentReportManager.getTest();

        if (extentTest != null) {
            extentTest.info("Scenario: " + scenario);
            extentTest.info(
                    "HTTP Status: " + response.getStatusCode()
            );
            extentTest.info(
                    "Response Content-Type: " + responseContentType
            );
        }
    }
}
