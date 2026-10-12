
package com.hunny.testforge.api;

import com.aventstack.extentreports.ExtentTest;
import com.hunny.testforge.base.ReportingBaseTest;
import com.hunny.testforge.reporting.ExtentReportManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static io.restassured.http.ContentType.JSON;

public class ApiBaseTest extends ReportingBaseTest {

    private static final int MAX_ERROR_BODY_LENGTH = 500;

    private static final int CONNECTION_TIMEOUT_MS = 5_000;
    private static final int SOCKET_TIMEOUT_MS = 10_000;

    private static final Pattern SENSITIVE_JSON_FIELD =
            Pattern.compile(
                    "(?i)(\"[^\"]*(?:password|passwd|token|authorization|auth|api.?key|secret|credential|email|phone|mobile|ssn|credit.?card|address|date.?of.?birth|cookie|session)[^\"]*\"\\s*:\\s*)(\"(?:\\\\.|[^\"\\\\])*\"|[^,}\\s]+)"
            );

    private static final Pattern SENSITIVE_QUERY_PARAMETER =
            Pattern.compile(
                    "(?i)(password|passwd|token|authorization|auth|api[_-]?key|secret|credential|email|phone|mobile|ssn|credit[_-]?card|address|date[_-]?of[_-]?birth|cookie|session|signature|(^|[_-])sig($|[_-]))"
            );

    protected RequestSpecification requestSpecification;

    @BeforeClass
    public void setupApi() {
        requestSpecification = buildRequestSpecification();
    }

    protected RequestSpecification getRequestSpecification() {
        if (requestSpecification == null) {
            requestSpecification = buildRequestSpecification();
        }

        return requestSpecification;
    }

    /**
     * Provides bounded connection and socket timeouts.
     * Use this configuration for other REST Assured requests
     * that do not inherit the shared API request specification.
     */
    public static RestAssuredConfig defaultRestAssuredConfig() {
        HttpClientConfig httpClientConfig =
                HttpClientConfig.httpClientConfig()
                        .setParam(
                                "http.connection.timeout",
                                CONNECTION_TIMEOUT_MS
                        )
                        .setParam(
                                "http.socket.timeout",
                                SOCKET_TIMEOUT_MS
                        );

        return RestAssuredConfig.config()
                .httpClient(httpClientConfig);
    }

    private RequestSpecification buildRequestSpecification() {
        return new RequestSpecBuilder()
                .setConfig(defaultRestAssuredConfig())
                .setBaseUri(ApiEndpoints.BASE_URL)
                .setContentType(JSON)
                .addFilter((requestSpec, responseSpec, context) -> {

                    long startNanos = System.nanoTime();

                    ExtentTest extentTest =
                            ExtentReportManager.getTest();

                    String requestDetails =
                            requestSpec.getMethod()
                                    + " "
                                    + sanitizeRequestUri(
                                            requestSpec.getURI()
                                    );

                    if (extentTest != null) {
                        extentTest.info(
                                "API Request: " + requestDetails
                        );
                    }

                    Response response;

                    try {
                        response = context.next(
                                requestSpec,
                                responseSpec
                        );
                    } catch (RuntimeException exception) {

                        if (extentTest != null) {
                            extentTest.warning(
                                    "API request execution failed or "
                                            + "timed out before a response "
                                            + "was received."
                            );

                            extentTest.info(
                                    "Exception type: "
                                            + exception.getClass()
                                                    .getSimpleName()
                            );
                        }

                        throw exception;
                    }

                    long responseTimeMs = TimeUnit.NANOSECONDS.toMillis(
                            System.nanoTime() - startNanos
                    );

                    int statusCode = response.getStatusCode();
                    String contentType =
                            response.getHeader("Content-Type");

                    if (extentTest != null) {

                        extentTest.info(
                                "HTTP Status Code: " + statusCode
                        );

                        extentTest.info(
                                "Response Content-Type: "
                                        + (contentType == null
                                                ? "Not provided"
                                                : contentType)
                        );

                        extentTest.info(
                                "Response Time: "
                                        + responseTimeMs + " ms"
                        );

                        if (statusCode >= 400) {
                            extentTest.warning(
                                    "HTTP error-status response received. "
                                            + "This may be expected for "
                                            + "negative API tests."
                            );

                            extentTest.info(
                                    "Error diagnostics: "
                                            + safeErrorResponseDetails(
                                                    response,
                                                    contentType
                                            )
                            );

                            logCorrelationId(response, extentTest);
                        }
                    }

                    return response;
                })
                .build();
    }

    // Package-private so unit tests can verify URI sanitization.
    static String sanitizeRequestUri(String requestUri) {
        try {
            URI uri = URI.create(requestUri);

            if (uri.getScheme() == null || uri.getHost() == null) {
                return "[URI omitted: could not safely sanitize]";
            }

            StringBuilder sanitized = new StringBuilder();

            sanitized.append(uri.getScheme())
                    .append("://")
                    .append(uri.getHost());

            if (uri.getPort() != -1) {
                sanitized.append(':').append(uri.getPort());
            }

            if (uri.getRawPath() != null) {
                sanitized.append(uri.getRawPath());
            }

            String rawQuery = uri.getRawQuery();

            if (rawQuery != null) {
                sanitized.append('?')
                        .append(sanitizeQueryParameters(rawQuery));
            }

            // User information and fragments are intentionally omitted.
            return sanitized.toString();

        } catch (IllegalArgumentException exception) {
            return "[URI omitted: could not safely sanitize]";
        }
    }

    // Package-private so unit tests can verify query masking.
    static String sanitizeQueryParameters(String rawQuery) {
        StringBuilder sanitized = new StringBuilder();
        String[] parameters = rawQuery.split("&", -1);

        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) {
                sanitized.append('&');
            }

            String parameter = parameters[i];
            int equalsIndex = parameter.indexOf('=');

            String rawName = equalsIndex >= 0
                    ? parameter.substring(0, equalsIndex)
                    : parameter;

            String decodedName;

            try {
                decodedName = URLDecoder.decode(
                        rawName,
                        StandardCharsets.UTF_8
                );
            } catch (IllegalArgumentException exception) {
                // If the name is malformed, hide its value.
                sanitized.append(rawName)
                        .append("=[REDACTED]");
                continue;
            }

            if (SENSITIVE_QUERY_PARAMETER
                    .matcher(decodedName)
                    .find()) {

                sanitized.append(rawName)
                        .append("=[REDACTED]");
            } else {
                sanitized.append(parameter);
            }
        }

        return sanitized.toString();
    }

    private static String safeErrorResponseDetails(
            Response response,
            String contentType) {

        if (contentType == null
                || !contentType.toLowerCase(Locale.ROOT)
                        .contains("json")) {

            return "Response body omitted because it is not identified "
                    + "as JSON.";
        }

        return sanitizeErrorResponseBody(response.asString());
    }

    // Package-private to enable isolated unit tests without HTTP calls.
    static String sanitizeErrorResponseBody(String body) {

        if (body == null || body.isBlank()) {
            return "JSON response body is empty.";
        }

        String sanitizedBody =
                SENSITIVE_JSON_FIELD.matcher(body)
                        .replaceAll("$1\"[REDACTED]\"");

        if (sanitizedBody.length() > MAX_ERROR_BODY_LENGTH) {
            sanitizedBody =
                    sanitizedBody.substring(0, MAX_ERROR_BODY_LENGTH)
                            + "... [truncated]";
        }

        return "Sanitized JSON response: " + sanitizedBody;
    }

    private static void logCorrelationId(
            Response response,
            ExtentTest extentTest) {

        String requestId = response.getHeader("X-Request-ID");

        if (requestId == null || requestId.isBlank()) {
            requestId = response.getHeader("X-Correlation-ID");
        }

        if (requestId != null
                && requestId.matches("[A-Za-z0-9._:-]{1,128}")) {

            extentTest.info(
                    "Response correlation ID: " + requestId
            );
        }
    }
}
