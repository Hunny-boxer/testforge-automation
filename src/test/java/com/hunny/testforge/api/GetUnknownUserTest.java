
package com.hunny.testforge.api;

import com.hunny.testforge.reporting.ExtentTestListener;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class GetUnknownUserTest extends ApiBaseTest {

    @Test(groups = {"API", "REGRESSION"})
    public void getUnknownUser() {

        Response response =
                given()
                        .spec(getRequestSpecification())
                        .pathParam("id", 999)
                        .when()
                        .get(ApiEndpoints.USER_BY_ID);

        Assert.assertEquals(
                response.getStatusCode(),
                200,
                "JSONPlaceholder returns HTTP 200 for unknown user IDs"
        );

        Assert.assertTrue(
                response.asString().trim().equals("{}")
                        || response.asString().trim().isEmpty(),
                "Unknown user response should contain no user data"
        );
    }
}
