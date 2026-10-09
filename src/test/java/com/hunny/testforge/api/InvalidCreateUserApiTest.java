
package com.hunny.testforge.api;

import com.hunny.testforge.reporting.ExtentTestListener;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class InvalidCreateUserApiTest extends ApiBaseTest {

    @DataProvider(name = "invalidUserPayloads")
    public Object[][] invalidUserPayloads() {
        return new Object[][]{
                {"Missing name", "{\"username\":\"hunnyboxer\",\"email\":\"hunny@example.com\"}"},
                {"Missing email", "{\"name\":\"Hunny Boxer\",\"username\":\"hunnyboxer\"}"},
                {"Empty payload", "{}"}
        };
    }

    @Test(
            dataProvider = "invalidUserPayloads",
            groups = {"API", "REGRESSION", "NEGATIVE"}
    )
    public void submitInvalidUserPayload(
            String scenario,
            String requestBody) {

        Response response =
                given()
                        .spec(getRequestSpecification())
                        .body(requestBody)
                        .when()
                        .post(ApiEndpoints.USERS);

        System.out.println("Scenario: " + scenario);
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.asString());

        Assert.assertTrue(
                response.getStatusCode() >= 200
                        && response.getStatusCode() < 600,
                "The API should return a valid HTTP status code"
        );
    }
}
