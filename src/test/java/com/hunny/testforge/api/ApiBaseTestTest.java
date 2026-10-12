
    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldRedactSensitiveJsonFields() {
        String body = "{\"password\":\"secret123\","
                + "\"access_token\":\"token-value\","
                + "\"email\":\"hunny@example.com\","
                + "\"name\":\"Hunny\",\"id\":42}";

        String result = ApiBaseTest.sanitizeErrorResponseBody(body);

        Assert.assertTrue(result.contains("\"password\":\"[REDACTED]\""));
        Assert.assertTrue(result.contains("\"access_token\":\"[REDACTED]\""));
        Assert.assertTrue(result.contains("\"email\":\"[REDACTED]\""));
        Assert.assertTrue(result.contains("\"name\":\"Hunny\""));
        Assert.assertTrue(result.contains("\"id\":42"));

        Assert.assertFalse(result.contains("secret123"));
        Assert.assertFalse(result.contains("token-value"));
        Assert.assertFalse(result.contains("hunny@example.com"));
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldHandleNullAndBlankErrorBodies() {
        Assert.assertEquals(
                ApiBaseTest.sanitizeErrorResponseBody(null),
                "JSON response body is empty."
        );

        Assert.assertEquals(
                ApiBaseTest.sanitizeErrorResponseBody(""),
                "JSON response body is empty."
        );

        Assert.assertEquals(
                ApiBaseTest.sanitizeErrorResponseBody("   "),
                "JSON response body is empty."
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldPreserveNonSensitiveJsonFields() {
        String body = "{\"message\":\"Invalid request\","
                + "\"status\":400,\"user\":\"Hunny\"}";

        String result = ApiBaseTest.sanitizeErrorResponseBody(body);

        Assert.assertEquals(
                result,
                "Sanitized JSON response: " + body
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldTruncateLongErrorBodiesAt500Characters() {
        String body = "{\"message\":\"" + "x".repeat(600) + "\"}";

        String result = ApiBaseTest.sanitizeErrorResponseBody(body);

        Assert.assertEquals(
                result,
                "Sanitized JSON response: "
                        + body.substring(0, 500)
                        + "... [truncated]"
        );

        Assert.assertTrue(result.endsWith("... [truncated]"));
    }
