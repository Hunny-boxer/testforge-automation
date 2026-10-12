
package com.hunny.testforge.api;

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

import static io.restassured.RestAssured.given;

@Listeners(ExtentTestListener.class)
public class AuthenticationNegativeApiTest {

    private HttpServer mockServer;
    private String baseUrl;

    @BeforeClass(alwaysRun = true)
    public void startMockServer() throws IOException {
        mockServer = HttpServer.create(
                new InetSocketAddress("127.0.0.1", 0),
                0
        );

        mockServer.createContext("/bearer", exchange -> {
            String authorization =
                    exchange.getRequestHeaders()
                            .getFirst("Authorization");

            if (authorization == null || authorization.isBlank()) {
                sendResponse(
                        exchange,
                        401,
                        "{\"authenticated\":false}"
                );
                return;
            }

            sendResponse(
                    exchange,
                    200,
                    "{\"authenticated\":true}"
            );
        });

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

    @Test(groups = {"API", "REGRESSION", "NEGATIVE"})
    public void protectedEndpointShouldRejectMissingBearerToken() {

        Response response =
                given()
                        .baseUri(baseUrl)
                        .when()
                        .get("/bearer");

        Assert.assertEquals(
                response.getStatusCode(),
                401,
                "A request without a bearer token should be unauthorized"
        );

        Assert.assertEquals(
                response.jsonPath().getBoolean("authenticated"),
                false
        );
    }

    private static void sendResponse(
            HttpExchange exchange,
            int status,
            String responseBody) throws IOException {

        byte[] body = responseBody.getBytes(StandardCharsets.UTF_8);

        try {
            exchange.getResponseHeaders().set(
                    "Content-Type",
                    "application/json; charset=UTF-8"
            );

            exchange.sendResponseHeaders(status, body.length);

            try (OutputStream output = exchange.getResponseBody()) {
                output.write(body);
            }
        } finally {
            exchange.close();
        }
    }
}
