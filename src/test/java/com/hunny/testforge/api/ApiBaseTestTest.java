
package com.hunny.testforge.api;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiBaseTestTest {

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldConfigureFiveSecondConnectionTimeout() {
        Object actual =
                ApiBaseTest.defaultRestAssuredConfig()
                        .getHttpClientConfig()
                        .params()
                        .get("http.connection.timeout");

        Assert.assertEquals(
                actual,
                Integer.valueOf(5_000),
                "Connection timeout should be 5 seconds"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldConfigureTenSecondSocketTimeout() {
        Object actual =
                ApiBaseTest.defaultRestAssuredConfig()
                        .getHttpClientConfig()
                        .params()
                        .get("http.socket.timeout");

        Assert.assertEquals(
                actual,
                Integer.valueOf(10_000),
                "Socket timeout should be 10 seconds"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldPreserveSafeQueryParameters() {
        String result = ApiBaseTest.sanitizeRequestUri(
                "https://api.example.test/users/1?sort=desc&page=2"
        );

        Assert.assertEquals(
                result,
                "https://api.example.test/users/1?sort=desc&page=2"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldRedactSensitiveQueryParameterValues() {
        String result = ApiBaseTest.sanitizeQueryParameters(
                "sort=desc&token=secret123&email=hunny@example.com"
        );

        Assert.assertEquals(
                result,
                "sort=desc&token=[REDACTED]&email=[REDACTED]"
        );

        Assert.assertFalse(result.contains("secret123"));
        Assert.assertFalse(result.contains("hunny@example.com"));
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldRedactEncodedSensitiveParameterNames() {
        String result = ApiBaseTest.sanitizeQueryParameters(
                "access%5Ftoken=hidden-value&page=2"
        );

        Assert.assertEquals(
                result,
                "access%5Ftoken=[REDACTED]&page=2"
        );

        Assert.assertFalse(result.contains("hidden-value"));
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldOmitMalformedRequestUri() {
        String result = ApiBaseTest.sanitizeRequestUri(
                "not a valid URI"
        );

        Assert.assertEquals(
                result,
                "[URI omitted: could not safely sanitize]"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldRedactValueWhenQueryParameterNameIsMalformed() {
        String result = ApiBaseTest.sanitizeQueryParameters(
                "%ZZ=private-value&page=2"
        );

        Assert.assertEquals(
                result,
                "%ZZ=[REDACTED]&page=2"
        );

        Assert.assertFalse(result.contains("private-value"));
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldOmitUserInformationAndFragmentFromUri() {
        String result = ApiBaseTest.sanitizeRequestUri(
                "https://user:password@api.example.test/users/1"
                        + "?page=2#private-section"
        );

        Assert.assertEquals(
                result,
                "https://api.example.test/users/1?page=2"
        );

        Assert.assertFalse(result.contains("password"));
        Assert.assertFalse(result.contains("private-section"));
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldRedactSensitiveJsonFields() {
        String body = "{\"password\":\"secret123\","
                + "\"access_token\":\"token-value\","
                + "\"email\":\"hunny@example.com\","
                + "\"name\":\"Hunny\",\"id\":42}";

        String result = ApiBaseTest.sanitizeErrorResponseBody(body);

        Assert.assertTrue(
                result.contains("\"password\":\"[REDACTED]\"")
        );
        Assert.assertTrue(
                result.contains("\"access_token\":\"[REDACTED]\"")
        );
        Assert.assertTrue(
                result.contains("\"email\":\"[REDACTED]\"")
        );
        Assert.assertTrue(result.contains("\"name\":\"Hunny\""));
        Assert.assertTrue(result.contains("\"id\":42"));

        Assert.assertFalse(result.contains("secret123"));
        Assert.assertFalse(result.contains("token-value"));
        Assert.assertFalse(result.contains("hunny@example.com"));
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldHandleNullAndBlankErrorBodies() {
        Assert.assertEquals(
                ApiBaseTest.sanitizeErrorResponseBody(null),
                "JSON response body is empty."
        );

        Assert.assertEquals(
                ApiBaseTest.sanitizeErrorResponseBody(""),
                "JSON response body is empty."
        );

        Assert.assertEquals(
                ApiBaseTest.sanitizeErrorResponseBody("   "),
                "JSON response body is empty."
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldPreserveNonSensitiveJsonFields() {
        String body = "{\"message\":\"Invalid request\","
                + "\"status\":400,\"user\":\"Hunny\"}";

        String result = ApiBaseTest.sanitizeErrorResponseBody(body);

        Assert.assertEquals(
                result,
                "Sanitized JSON response: " + body
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldTruncateLongErrorBodiesAt500Characters() {
        String body = "{\"message\":\"" + "x".repeat(600) + "\"}";

        String result = ApiBaseTest.sanitizeErrorResponseBody(body);

        Assert.assertEquals(
                result,
                "Sanitized JSON response: "
                        + body.substring(0, 500)
                        + "... [truncated]"
        );

        Assert.assertTrue(result.endsWith("... [truncated]"));
    }
}
