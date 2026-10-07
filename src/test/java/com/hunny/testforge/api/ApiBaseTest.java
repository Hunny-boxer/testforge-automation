package com.hunny.testforge.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.http.ContentType.JSON;

public class ApiBaseTest {

    protected RequestSpecification requestSpecification;

    @BeforeClass
    public void setupApi() {

        enableLoggingOfRequestAndResponseIfValidationFails();

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(ApiEndpoints.BASE_URL)
                .setContentType(JSON)
                .build();
    }
}
