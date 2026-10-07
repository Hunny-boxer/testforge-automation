package com.hunny.testforge.api;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

public class ApiResponseSpecification {

    private ApiResponseSpecification() {
        // Utility class
    }

    public static ResponseSpecification successResponse() {

        return new ResponseSpecBuilder()
                .expectContentType("application/json")
                .build();
    }
}
