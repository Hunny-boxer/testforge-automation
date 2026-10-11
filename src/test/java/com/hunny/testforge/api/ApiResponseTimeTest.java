package com.hunny.testforge.api;

import com.hunny.testforge.reporting.ExtentReportManager;
import com.hunny.testforge.reporting.ExtentTestListener;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class ApiResponseTimeTest extends ApiBaseTest {

    private static final long MAX_RESPONSE_TIME_MS = 10000;

    @DataProvider(name = "apiEndpoints")
    public Object[][] apiEndpoints() {
        return new Object[][]{
                {"Get user", ApiEndpoints.USER_BY_ID, 1},
                {"Get post", ApiEndpoints.POST_BY_ID, 1}
        };
    }

    @Test(
            dataProvider = "apiEndpoints",
            groups = {"API", "REGRESSION", "PERFORMANCE"}
    )
    public void apiResponseShouldBeWithinTimeLimit(
            String scenario,
            String endpoint,
            int id) {

        Response response =
                given()
                        .spec(getRequestSpecification())
                        .pathParam("id", id)
                        .when()
                        .get(endpoint);

        long responseTimeMs =
                response.getTimeIn(TimeUnit.MILLISECONDS);

        int statusCode = response.getStatusCode();

        // Keep the details in the Maven console.
        System.out.println("Scenario: " + scenario);
        System.out.println("Method: GET");
        System.out.println("Endpoint: " + endpoint);
        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Time: " + responseTimeMs + " ms");
        System.out.println(
                "Response Time Limit: " + MAX_RESPONSE_TIME_MS + " ms"
        );

        // Also add the details to the ExtentReport HTML test entry.
        ExtentTest extentTest = ExtentReportManager.getTest();

        if (extentTest != null) {
            extentTest.info("Scenario: " + scenario);
            extentTest.info("Request Method: GET");
            extentTest.info("Endpoint: " + endpoint);
            extentTest.info("HTTP Status Code: " + statusCode);
            extentTest.info(
                    "Actual Response Time: " + responseTimeMs + " ms"
            );
            extentTest.info(
                    "Maximum Allowed Response Time: "
                            + MAX_RESPONSE_TIME_MS + " ms"
            );
        }

        // Validate the HTTP response.
        Assert.assertEquals(
                statusCode,
                200,
                scenario + " should return HTTP 200"
        );

        // Validate the response-time threshold.
        Assert.assertTrue(
                responseTimeMs < MAX_RESPONSE_TIME_MS,
                scenario + " took " + responseTimeMs
                        + " ms; expected less than "
                        + MAX_RESPONSE_TIME_MS + " ms"
        );

        if (extentTest != null) {
            extentTest.pass(
                    "HTTP status and response-time checks passed."
            );
        }
    }
}
