package com.hunny.testforge.api;

import com.hunny.testforge.reporting.ExtentTestListener;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class NegativeUserApiTest extends ApiBaseTest {

    @Test
    public void getNonExistingUser() {

        Response response =
                given()
                        .spec(requestSpecification)
                        .pathParam("id", 9999)
                        .when()
                        .get(ApiEndpoints.USER_BY_ID);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.asString());

        Assert.assertEquals(
                response.getStatusCode(),
                404,
                "Expected HTTP status code 404 for non-existing user"
        );
    }
}
