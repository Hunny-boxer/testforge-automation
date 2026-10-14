
package com.hunny.testforge.api;

import io.restassured.response.Response;
import org.testng.Assert;

public final class ApiSecurityAssertions {

    private ApiSecurityAssertions() {
        // Utility class
    }

    public static void assertSecureJsonResponse(
            Response response,
            int expectedStatusCode) {

        Assert.assertNotNull(
                response,
                "API response must not be null"
        );

        Assert.assertEquals(
                response.getStatusCode(),
                expectedStatusCode,
                "Unexpected HTTP status code"
        );

        String contentType = response.getHeader("Content-Type");

        Assert.assertNotNull(
                contentType,
                "Content-Type header must be present"
        );

        Assert.assertTrue(
                ApiBaseTest.isJsonContentType(contentType),
                "Expected application/json Content-Type "
                        + "(or application/*+json)"
        );

        Assert.assertEquals(
                response.getHeader("X-Content-Type-Options"),
                "nosniff",
                "Expected X-Content-Type-Options: nosniff"
        );

        assertHstsIfPresent(
                response.getHeader("Strict-Transport-Security")
        );

        assertCspIfPresent(
                response.getHeader("Content-Security-Policy")
        );
    }

    private static void assertHstsIfPresent(String hsts) {
        if (hsts == null) {
            return;
        }

        Assert.assertTrue(
                hsts.matches("(?i).*max-age\\s*=\\s*[1-9]\\d*.*"),
                "Present HSTS header must specify a positive max-age"
        );
    }

    private static void assertCspIfPresent(String csp) {
        if (csp == null) {
            return;
        }

        Assert.assertFalse(
                csp.isBlank(),
                "Content-Security-Policy must not be blank"
        );
    }
}
