
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
                404,
                "Expected HTTP 404 for an unknown user ID"
        );

        Assert.assertTrue(
                response.asString().trim().isEmpty()
                        || response.asString().trim().equals("{}"),
                "Unknown user response should contain no user data"
        );
    }
}
