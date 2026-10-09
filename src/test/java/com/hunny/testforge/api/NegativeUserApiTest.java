```java
package com.hunny.testforge.api;

import com.hunny.testforge.reporting.ExtentTestListener;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class NegativeUserApiTest extends ApiBaseTest {

    @DataProvider(name = "invalidUserIds")
    public Object[][] invalidUserIds() {
        return new Object[][]{
                {9999},
                {99999},
                {123456}
        };
    }

    @Test(
            dataProvider = "invalidUserIds",
            groups = {"API", "NEGATIVE"}
    )
    public void getNonExistingUser(int userId) {

        Response response =
                given()
                        .spec(getRequestSpecification())
                        .pathParam("id", userId)
                        .when()
                        .get(ApiEndpoints.USER_BY_ID);

        System.out.println(
                "Testing invalid user ID: " + userId
        );

        System.out.println(
                "Status Code: " + response.getStatusCode()
        );

        System.out.println(
                "Response Body: " + response.asString()
        );

        Assert.assertEquals(
                response.getStatusCode(),
                404,
                "Expected HTTP 404 for non-existing user ID: " + userId
        );

        Assert.assertTrue(
                response.asString().trim().isEmpty()
                        || response.asString().trim().equals("{}"),
                "Expected an empty response for invalid user ID: " + userId
        );
    }
}
```
