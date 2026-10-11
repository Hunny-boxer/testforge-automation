
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
                {
                        "Missing name",
                        "{\"username\":\"hunnyboxer\",\"email\":\"hunny@example.com\"}",
                        "name"
                },
                {
                        "Missing email",
                        "{\"name\":\"Hunny Boxer\",\"username\":\"hunnyboxer\"}",
                        "email"
                },
                {
                        "Empty payload",
                        "{}",
                        "name"
                }
        };
    }

    @Test(
            dataProvider = "invalidUserPayloads",
            groups = {"API", "REGRESSION", "MOCK_BEHAVIOR"}
    )
    public void jsonPlaceholderAcceptsIncompleteUserPayload(
            String scenario,
            String requestBody,
            String missingField) {

        Response response =
                given()
                        .spec(getRequestSpecification())
                        .body(requestBody)
                        .when()
                        .post(ApiEndpoints.USERS);

        System.out.println("Scenario: " + scenario);
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.asString());

        Assert.assertEquals(
                response.getStatusCode(),
                201,
                "JSONPlaceholder should simulate a successful POST"
        );

        int generatedId = response.jsonPath().getInt("id");

        Assert.assertTrue(
                generatedId > 0,
                "The simulated response should contain a positive ID"
        );

        Assert.assertNull(
                response.jsonPath().getString(missingField),
                "The mock should not invent the missing field: " + missingField
        );
    }
}
