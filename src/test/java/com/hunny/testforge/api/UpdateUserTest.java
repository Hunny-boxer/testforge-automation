package com.hunny.testforge.api;

import com.hunny.testforge.reporting.ExtentTestListener;
import com.hunny.testforge.utils.TestDataReader;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class UpdateUserTest extends ApiBaseTest {

    @Test(groups = {"API", "REGRESSION"})
    public void updateUser() {

        String requestBody =
                TestDataReader.readJsonFile("testdata/update-user.json");

        Response response =
                given()
                        .spec(getRequestSpecification())
                        .pathParam("id", 1)
                        .body(requestBody)
                        .when()
                        .put(ApiEndpoints.USER_BY_ID)
                        .then()
                        .spec(ApiResponseSpecification.successResponse(200))
                        .extract()
                        .response();

        System.out.println(
                "Status Code: " + response.getStatusCode()
        );

        System.out.println(
                "Response Body: " + response.asString()
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
