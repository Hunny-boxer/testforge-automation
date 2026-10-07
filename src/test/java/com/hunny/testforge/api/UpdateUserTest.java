package com.hunny.testforge.api;

import com.hunny.testforge.reporting.ExtentTestListener;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class UpdateUserTest extends ApiBaseTest {

    @Test
    public void updateUser() {

        String requestBody = """
                {
                    "id": 1,
                    "name": "Hunny Boxer Updated",
                    "username": "hunny_updated",
                    "email": "hunny.updated@example.com"
                }
                """;

        Response response =
                given()
                        .spec(requestSpecification)
                        .pathParam("id", 1)
                        .body(requestBody)
                        .when()
                        .put(ApiEndpoints.USER_BY_ID);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.asString());

        Assert.assertEquals(
                response.getStatusCode(),
                200,
                "Expected HTTP status code 200"
        );

        Assert.assertEquals(
                response.jsonPath().getString("name"),
                "Hunny Boxer Updated",
                "Updated name should match"
        );

        Assert.assertEquals(
                response.jsonPath().getString("username"),
                "hunny_updated",
                "Updated username should match"
        );
    }
}
