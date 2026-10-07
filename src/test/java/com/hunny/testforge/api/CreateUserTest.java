package com.hunny.testforge.api;

import com.hunny.testforge.reporting.ExtentTestListener;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class CreateUserTest extends ApiBaseTest {

    @Test
    public void createUser() {

        String requestBody = """
                {
                    "name": "Hunny Boxer",
                    "username": "hunnyboxer",
                    "email": "hunny@example.com"
                }
                """;

        Response response =
                given()
                        .spec(requestSpecification)
                        .body(requestBody)
                        .when()
                        .post(ApiEndpoints.USERS);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.asString());

        Assert.assertEquals(
                response.getStatusCode(),
                201,
                "Expected HTTP status code 201"
        );

        Assert.assertEquals(
                response.jsonPath().getString("name"),
                "Hunny Boxer",
                "Created user name should match"
        );

        Assert.assertEquals(
                response.jsonPath().getString("username"),
                "hunnyboxer",
                "Created username should match"
        );

        Assert.assertNotNull(
                response.jsonPath().getString("id"),
                "Created user ID should not be null"
        );
    }
}
