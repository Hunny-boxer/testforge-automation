
package com.hunny.testforge.api;

import com.hunny.testforge.reporting.ExtentTestListener;
import io.restassured.response.Response;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class ApiSecurityHeadersTest extends ApiBaseTest {

    @Test(groups = {"API", "REGRESSION", "SECURITY"})
    public void userEndpointShouldReturnSecureJsonHeaders() {

        Response response =
                given()
                        .spec(getRequestSpecification())
                        .pathParam("id", 1)
                        .when()
                        .get(ApiEndpoints.USER_BY_ID);

        ApiSecurityAssertions.assertSecureJsonResponse(
                response,
                200
        );
    }
}
