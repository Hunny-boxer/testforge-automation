
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

        Assert.assertEquals(
                response.getStatusCode(),
                200,
                "Expected HTTP 200 for user retrieval"
        );

        Assert.assertEquals(
                response.getHeader("X-Content-Type-Options"),
                "nosniff",
                "User response should disable MIME-type sniffing"
        );

        Assert.assertEquals(
                response.jsonPath().getInt("id"),
                1,
                "User ID should be 1"
        );

        Assert.assertTrue(
                response.jsonPath().getString("name") != null
                        && !response.jsonPath().getString("name").isBlank(),
                "User name should not be empty"
        );

        Assert.assertTrue(
                response.jsonPath().getString("email") != null
                        && !response.jsonPath().getString("email").isBlank(),
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
