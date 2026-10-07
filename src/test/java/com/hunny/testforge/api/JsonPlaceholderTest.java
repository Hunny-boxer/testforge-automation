package com.hunny.testforge.api;

import com.hunny.testforge.reporting.ExtentTestListener;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class JsonPlaceholderTest extends ApiBaseTest {

    @Test
    public void getUserDetails() {

        Response response =
                given()
                        .spec(requestSpecification)
                        .pathParam("id", 1)
                        .when()
                        .get(ApiEndpoints.USER_BY_ID)
                        .then()
                        .spec(ApiResponseSpecification.successResponse())
                        .extract()
                        .response();

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.asString());

        Assert.assertEquals(
                response.getStatusCode(),
                200,
                "Expected HTTP status code 200"
        );

        Assert.assertEquals(
                response.jsonPath().getInt("id"),
                1,
                "User ID should be 1"
        );

        Assert.assertNotNull(
                response.jsonPath().getString("name"),
                "User name should not be null"
        );
    }
}
