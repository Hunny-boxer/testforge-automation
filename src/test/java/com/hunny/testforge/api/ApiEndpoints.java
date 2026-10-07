package com.hunny.testforge.api;

public class ApiEndpoints {

    private ApiEndpoints() {
        // Utility class
    }

    public static final String BASE_URL =
            "https://jsonplaceholder.typicode.com";

    public static final String USERS =
            "/users";

    public static final String USER_BY_ID =
            "/users/{id}";

    public static final String POSTS =
            "/posts";

    public static final String POST_BY_ID =
            "/posts/{id}";
}
