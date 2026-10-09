package com.hunny.testforge.api;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

public class ApiResponseSpecification {

    private ApiResponseSpecification() {
        // Utility class
    }

    public static ResponseSpecification successResponse() {

        return successResponse(200);
    }

    public static ResponseSpecification successResponse(
            int expectedStatusCode) {

        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .expectContentType("application/json")
                .build();
    }
}
