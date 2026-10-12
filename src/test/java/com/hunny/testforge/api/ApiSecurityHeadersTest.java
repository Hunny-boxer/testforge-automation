
package com.hunny.testforge.api;

import com.hunny.testforge.reporting.ExtentTestListener;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Locale;

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

        Assert.assertEquals(
                response.getStatusCode(),
                200,
                "Expected HTTP 200 from the user endpoint"
        );

        String contentType = response.getHeader("Content-Type");

        Assert.assertNotNull(
                contentType,
                "Content-Type header must be present"
        );

        Assert.assertTrue(
                contentType.toLowerCase(Locale.ROOT)
                        .contains("application/json"),
                "Expected a JSON response Content-Type"
        );

        Assert.assertEquals(
                response.getHeader("X-Content-Type-Options"),
                "nosniff",
                "Expected nosniff to prevent MIME-type sniffing"
        );

        // HSTS is optional for this API response.
        String hsts =
                response.getHeader("Strict-Transport-Security");

        if (hsts != null && !hsts.isBlank()) {
            Assert.assertTrue(
                    hsts.toLowerCase(Locale.ROOT)
                            .matches(".*max-age\\s*=\\s*[1-9]\\d*.*"),
                    "If present, HSTS should specify a positive max-age"
            );
        }

        // CSP is also optional; do not fail solely because it is absent.
        String csp =
                response.getHeader("Content-Security-Policy");

        if (csp != null) {
            Assert.assertFalse(
                    csp.isBlank(),
                    "Content-Security-Policy must not be blank when present"
            );
        }
    }
}
