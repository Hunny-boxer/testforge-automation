
package com.hunny.testforge.api;

import com.hunny.testforge.base.ReportingBaseTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

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
                .build();
    }
}
