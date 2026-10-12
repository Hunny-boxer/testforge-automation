
package com.hunny.testforge.api;

import com.hunny.testforge.reporting.ExtentTestListener;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class AuthenticationNegativeApiTest {

    private static final String BASE_URL = "https://httpbin.org";

    @Test(groups = {"API", "REGRESSION", "NEGATIVE"})
    public void protectedEndpointShouldRejectMissingBearerToken() {

        Response response =
                given()
                        .baseUri(BASE_URL)
                        .when()
                        .get("/bearer");

        Assert.assertEquals(
                response.getStatusCode(),
                401,
                "Expected 401 Unauthorized without a bearer token"
        );
    }
}
