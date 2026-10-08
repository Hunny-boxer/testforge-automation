package com.hunny.testforge.api;

import com.hunny.testforge.reporting.ExtentTestListener;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class DeleteUserTest extends ApiBaseTest {

    @Test(groups = {"API", "REGRESSION"})
    public void deleteUser() {

        Response response =
                given()
                        .spec(requestSpecification)
                        .pathParam("id", 1)
                        .when()
                        .delete(ApiEndpoints.USER_BY_ID)
                        .then()
                        .spec(ApiResponseSpecification.successResponse())
                        .extract()
                        .response();

        System.out.println(
                "Status Code: " + response.getStatusCode()
        );

        System.out.println(
                "Response Body: " + response.asString()
        );

        Assert.assertEquals(
                response.getStatusCode(),
                200,
                "Expected HTTP status code 200"
        );
    }
}
