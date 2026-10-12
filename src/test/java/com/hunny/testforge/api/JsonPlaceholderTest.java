
package com.hunny.testforge.api;

import com.hunny.testforge.reporting.ExtentTestListener;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Listeners(ExtentTestListener.class)
public class JsonPlaceholderTest extends ApiBaseTest {

    @Test(groups = {"API", "SMOKE"})
    public void getUserDetails() {

        Response response =
                given()
                        .spec(getRequestSpecification())
                        .pathParam("id", 1)
                        .when()
                        .get(ApiEndpoints.USER_BY_ID)
                        .then()
                        .spec(ApiResponseSpecification.successResponse())
                        .body(matchesJsonSchemaInClasspath(
                                "schemas/user-schema.json"
                        ))
                        .extract()
                        .response();

        // Reusable status, Content-Type and security-header checks.
        ApiSecurityAssertions.assertSecureJsonResponse(
                response,
                200
        );

        Assert.assertEquals(
                response.jsonPath().getInt("id"),
                1,
                "User ID should be 1"
        );

        String name = response.jsonPath().getString("name");
        String email = response.jsonPath().getString("email");

        Assert.assertTrue(
                name != null && !name.isBlank(),
                "User name should not be empty"
        );

        Assert.assertTrue(
                email != null && !email.isBlank(),
                "User email should not be empty"
        );
    }

    @Test(groups = {"API", "SMOKE"})
    public void getPostDetails() {

        Response response =
                given()
                        .spec(getRequestSpecification())
                        .pathParam("id", 1)
                        .when()
                        .get(ApiEndpoints.POST_BY_ID)
                        .then()
                        .spec(ApiResponseSpecification.successResponse())
                        .body(matchesJsonSchemaInClasspath(
                                "schemas/post-schema.json"
                        ))
                        .extract()
                        .response();

        System.out.println(
                "Post response status: " + response.getStatusCode()
        );
        System.out.println(
                "Post response body: " + response.asString()
        );

        Assert.assertEquals(
                response.getStatusCode(),
                200,
                "Expected HTTP 200 for post retrieval"
        );

        Assert.assertEquals(
                response.jsonPath().getInt("id"),
                1,
                "Post ID should be 1"
        );

        Assert.assertEquals(
                response.jsonPath().getInt("userId"),
                1,
                "Post should belong to user ID 1"
        );

        String title = response.jsonPath().getString("title");
        String body = response.jsonPath().getString("body");

        Assert.assertTrue(
                title != null && !title.isBlank(),
                "Post title should not be empty"
        );

        Assert.assertTrue(
                body != null && !body.isBlank(),
                "Post body should not be empty"
        );
    }
}
