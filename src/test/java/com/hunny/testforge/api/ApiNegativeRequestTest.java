
package com.hunny.testforge.api;

import com.aventstack.extentreports.ExtentTest;
import com.hunny.testforge.reporting.ExtentReportManager;
import com.hunny.testforge.reporting.ExtentTestListener;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class ApiNegativeRequestTest {

    private HttpServer mockServer;
    private String baseUrl;

    @BeforeClass(alwaysRun = true)
    public void startMockServer() throws IOException {
        mockServer = HttpServer.create(
                new InetSocketAddress("127.0.0.1", 0),
                0
        );

        mockServer.createContext("/anything", exchange -> {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(
                        exchange,
                        405,
                        "application/json",
                        "{\"error\":\"Method not allowed\"}"
                );
                return;
            }

            String requestBody = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            String requestContentType =
                    exchange.getRequestHeaders().getFirst("Content-Type");

            if (requestContentType == null) {
                requestContentType = "";
            }

            String responseBody =
                    "{\"data\":\"" + escapeJson(requestBody)
                    + "\",\"headers\":{\"Content-Type\":\""
                    + escapeJson(requestContentType) + "\"}}";

            sendResponse(
                    exchange,
                    200,
                    "application/json; charset=UTF-8",
                    responseBody
            );
        });

        mockServer.createContext("/xml", exchange ->
                sendResponse(
                        exchange,
                        200,
                        "application/xml; charset=UTF-8",
                        "<note><message>XML fixture</message></note>"
                )
        );

        mockServer.start();

        baseUrl = "http://127.0.0.1:"
                + mockServer.getAddress().getPort();
    }

    @AfterClass(alwaysRun = true)
    public void stopMockServer() {
        if (mockServer != null) {
            mockServer.stop(0);
            mockServer = null;
        }
    }

    @Test(groups = {"API", "REGRESSION", "NEGATIVE", "SECURITY"})
    public void malformedJsonPayloadShouldBePreservedAsRawRequestData() {

        String malformedJson = "{\"name\":";

        Response response =
                given()
                        .baseUri(baseUrl)
                        .contentType("application/json")
                        .body(malformedJson)
                        .when()
                        .post("/anything");

        logDiagnostics(
                "Malformed JSON request",
                "application/json",
                response
        );

        Assert.assertEquals(response.getStatusCode(), 200);

        Assert.assertEquals(
                response.jsonPath().getString("data"),
                malformedJson,
                "The fixture should preserve the malformed payload"
        );

        Assert.assertEquals(
                echoedRequestMediaType(response),
                "application/json"
        );
    }

    @Test(groups = {"API", "REGRESSION", "NEGATIVE", "SECURITY"})
    public void nonJsonRequestContentTypeShouldBePreserved() {

        String requestBody = "<user><name>Hunny</name></user>";

        Response response =
                given()
                        .baseUri(baseUrl)
                        .contentType("text/plain")
                        .body(requestBody)
                        .when()
                        .post("/anything");

        logDiagnostics(
                "Non-JSON request Content-Type",
                "text/plain",
                response
        );

        Assert.assertEquals(response.getStatusCode(), 200);

        Assert.assertEquals(
                response.jsonPath().getString("data"),
                requestBody,
                "The fixture should preserve the request body"
        );

        Assert.assertEquals(
                echoedRequestMediaType(response),
                "text/plain"
        );
    }

    @Test(groups = {"API", "REGRESSION", "NEGATIVE", "SECURITY"})
    public void securityAssertionsShouldRejectXmlAsJson() {

        Response response =
                given()
                        .baseUri(baseUrl)
                        .when()
                        .get("/xml");

        logDiagnostics(
                "Reject non-JSON response",
                "Not applicable - GET request",
                response
        );

        Assert.assertEquals(response.getStatusCode(), 200);

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
                "The helper should reject the XML response"
        );
    }

    private String echoedRequestMediaType(Response response) {
        Map<String, Object> headers =
                response.jsonPath().getMap("headers");

        Assert.assertNotNull(
                headers,
                "The local fixture should return echoed headers"
        );

        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            if ("Content-Type".equalsIgnoreCase(entry.getKey())) {
                return String.valueOf(entry.getValue())
                        .split(";", 2)[0]
                        .trim()
                        .toLowerCase(Locale.ROOT);
            }
        }

        Assert.fail("Echoed Content-Type header was not found");
        return null;
    }

    private static String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static void sendResponse(
            HttpExchange exchange,
            int status,
            String contentType,
            String responseBody) throws IOException {

        byte[] body = responseBody.getBytes(StandardCharsets.UTF_8);

        try {
            exchange.getResponseHeaders().set(
                    "Content-Type",
                    contentType
            );

            exchange.sendResponseHeaders(status, body.length);

            try (OutputStream output = exchange.getResponseBody()) {
                output.write(body);
            }
        } finally {
            exchange.close();
        }
    }

    private void logDiagnostics(
            String scenario,
            String requestContentType,
            Response response) {

        String responseContentType =
                response.getHeader("Content-Type");

        if (responseContentType == null) {
            responseContentType = "Not provided";
        }

        System.out.println("Scenario: " + scenario);
        System.out.println("Request Content-Type: " + requestContentType);
        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Content-Type: " + responseContentType);

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
                    "Response Content-Type: " + responseContentType
            );
        }
    }
}
