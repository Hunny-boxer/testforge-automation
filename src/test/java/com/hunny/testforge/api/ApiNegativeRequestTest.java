
package com.hunny.testforge.api;

import com.aventstack.extentreports.ExtentTest;
import com.hunny.testforge.reporting.ExtentReportManager;
import com.hunny.testforge.reporting.ExtentTestListener;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Locale;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class ApiNegativeRequestTest {

    private static final String BASE_URL = "https://httpbin.org";

    @Test(groups = {"API", "REGRESSION", "NEGATIVE", "SECURITY"})
    public void malformedJsonPayloadShouldBePreservedAsRawRequestData() {

        String malformedJson = "{\"name\":";

        Response response =
                given()
                        .baseUri(BASE_URL)
                        .contentType("application/json")
                        .body(malformedJson)
                        .when()
                        .post("/anything");

        logDiagnostics(
                "Malformed JSON request",
                "application/json",
                response
        );

        Assert.assertEquals(
                response.getStatusCode(),
                200,
                "The HTTPBin echo endpoint should return HTTP 200"
        );

        Assert.assertEquals(
                response.jsonPath().getString("data"),
                malformedJson,
                "The echo response should preserve the malformed payload"
        );

        Assert.assertEquals(
                getEchoedRequestMediaType(response, "Content-Type"),
                "application/json",
                "The echoed request media type should be application/json"
        );
    }

    @Test(groups = {"API", "REGRESSION", "NEGATIVE", "SECURITY"})
    public void nonJsonRequestContentTypeShouldBePreserved() {

        String requestBody = "<user><name>Hunny</name></user>";

        Response response =
                given()
                        .baseUri(BASE_URL)
                        .contentType("text/plain")
                        .body(requestBody)
                        .when()
                        .post("/anything");

        logDiagnostics(
                "Non-JSON request Content-Type",
                "text/plain",
                response
        );

        Assert.assertEquals(
                response.getStatusCode(),
                200,
                "The HTTPBin echo endpoint should return HTTP 200"
        );

        Assert.assertEquals(
                response.jsonPath().getString("data"),
                requestBody,
                "The echo response should preserve the request body"
        );

        Assert.assertEquals(
                getEchoedRequestMediaType(response, "Content-Type"),
                "text/plain",
                "The echoed request media type should remain text/plain"
        );
    }

    @Test(groups = {"API", "REGRESSION", "NEGATIVE", "SECURITY"})
    public void securityAssertionsShouldRejectXmlAsJson() {

        Response response =
                given()
                        .baseUri(BASE_URL)
                        .when()
                        .get("/xml");

        logDiagnostics(
                "Reject non-JSON response",
                "Not applicable - GET request",
                response
        );

        Assert.assertEquals(
                response.getStatusCode(),
                200,
                "The XML fixture should return HTTP 200"
        );

        String contentType = response.getHeader("Content-Type");

        Assert.assertNotNull(
                contentType,
                "The XML fixture should provide a Content-Type header"
        );

        Assert.assertFalse(
                contentType.toLowerCase(Locale.ROOT)
                        .contains("application/json"),
                "The XML fixture must not identify itself as JSON"
        );

        AssertionError error = Assert.expectThrows(
                AssertionError.class,
                () -> ApiSecurityAssertions.assertSecureJsonResponse(
                        response,
                        200
                )
        );

        Assert.assertTrue(
                error.getMessage().contains(
                        "Expected application/json Content-Type"
                ),
                "The security helper should reject a non-JSON response"
        );
    }

    private String getEchoedRequestMediaType(
            Response response,
            String headerName) {

        String contentType =
                getEchoedRequestHeader(response, headerName);

        // Ignore optional parameters such as charset=ISO-8859-1.
        return contentType.split(";", 2)[0]
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    private String getEchoedRequestHeader(
            Response response,
            String headerName) {

        Map<String, Object> headers =
                response.jsonPath().getMap("headers");

        Assert.assertNotNull(
                headers,
                "HTTPBin should return the echoed request headers"
        );

        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(headerName)) {
                return String.valueOf(entry.getValue());
            }
        }

        Assert.fail(
                "Echoed request header was not found: " + headerName
        );

        return null;
    }

    private void logDiagnostics(
            String scenario,
            String requestContentType,
            Response response) {

        String responseContentType =
                response.getHeader("Content-Type");

        String safeResponseContentType =
                responseContentType == null
                        ? "Not provided"
                        : responseContentType;

        System.out.println("Scenario: " + scenario);
        System.out.println(
                "Request Content-Type: " + requestContentType
        );
        System.out.println(
                "Response Status: " + response.getStatusCode()
        );
        System.out.println(
                "Response Content-Type: " + safeResponseContentType
        );

        ExtentTest extentTest = ExtentReportManager.getTest();

        if (extentTest != null) {
            extentTest.info("Scenario: " + scenario);
            extentTest.info(
                    "Request Content-Type: " + requestContentType
            );
            extentTest.info(
                    "HTTP Status Code: " + response.getStatusCode()
            );
            extentTest.info(
                    "Response Content-Type: " + safeResponseContentType
            );
        }
    }
}
