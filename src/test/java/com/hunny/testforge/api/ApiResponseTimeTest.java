
package com.hunny.testforge.api;

import com.hunny.testforge.reporting.ExtentTestListener;
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

        System.out.println("Scenario: " + scenario);
        System.out.println("Endpoint: " + endpoint);
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Time: " + responseTimeMs + " ms");

        Assert.assertEquals(
                response.getStatusCode(),
                200,
                scenario + " should return HTTP 200"
        );

        Assert.assertTrue(
                responseTimeMs < MAX_RESPONSE_TIME_MS,
                scenario + " took " + responseTimeMs
                        + " ms; expected less than "
                        + MAX_RESPONSE_TIME_MS + " ms"
        );
    }
}
