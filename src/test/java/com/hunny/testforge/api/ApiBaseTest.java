
package com.hunny.testforge.api;

import com.aventstack.extentreports.ExtentTest;
import com.hunny.testforge.base.ReportingBaseTest;
import com.hunny.testforge.reporting.ExtentReportManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.http.ContentType.JSON;

public class ApiBaseTest extends ReportingBaseTest {

    protected RequestSpecification requestSpecification;

    @BeforeClass
    public void setupApi() {
        enableLoggingOfRequestAndResponseIfValidationFails();
        requestSpecification = buildRequestSpecification();
    }

    protected RequestSpecification getRequestSpecification() {
        if (requestSpecification == null) {
            requestSpecification = buildRequestSpecification();
        }
        return requestSpecification;
    }

    private RequestSpecification buildRequestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(ApiEndpoints.BASE_URL)
                .setContentType(JSON)
                .addFilter((requestSpec, responseSpec, context) -> {

                    long startNanos = System.nanoTime();

                    ExtentTest extentTest =
                            ExtentReportManager.getTest();

                    String requestDetails =
                            requestSpec.getMethod()
                                    + " "
                                    + requestSpec.getURI();

                    if (extentTest != null) {
                        extentTest.info(
                                "API Request: " + requestDetails
                        );
                    }

                    Response response =
                            context.next(requestSpec, responseSpec);

                    long responseTimeMs = TimeUnit.NANOSECONDS.toMillis(
                            System.nanoTime() - startNanos
                    );

                    if (extentTest != null) {
                        extentTest.info(
                                "HTTP Status Code: "
                                        + response.getStatusCode()
                        );

                        extentTest.info(
                                "Response Content-Type: "
                                        + response.getHeader("Content-Type")
                        );

                        extentTest.info(
                                "Response Time: "
                                        + responseTimeMs + " ms"
                        );
                    }

                    return response;
                })
                .build();
    }
}
