
package com.hunny.testforge.api;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiBaseTestTest {

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldPreserveSafeQueryParameters() {
        String result = ApiBaseTest.sanitizeRequestUri(
                "https://api.example.test/users/1?sort=desc&page=2"
        );

        Assert.assertEquals(
                result,
                "https://api.example.test/users/1?sort=desc&page=2"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldRedactSensitiveQueryParameterValues() {
        String result = ApiBaseTest.sanitizeQueryParameters(
                "sort=desc&token=secret123&email=hunny@example.com"
        );

        Assert.assertEquals(
                result,
                "sort=desc&token=[REDACTED]&email=[REDACTED]"
        );

        Assert.assertFalse(result.contains("secret123"));
        Assert.assertFalse(result.contains("hunny@example.com"));
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldRedactEncodedSensitiveParameterNames() {
        String result = ApiBaseTest.sanitizeQueryParameters(
                "access%5Ftoken=hidden-value&page=2"
        );

        Assert.assertEquals(
                result,
                "access%5Ftoken=[REDACTED]&page=2"
        );

        Assert.assertFalse(result.contains("hidden-value"));
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldOmitMalformedRequestUri() {
        String result = ApiBaseTest.sanitizeRequestUri(
                "not a valid URI"
        );

        Assert.assertEquals(
                result,
                "[URI omitted: could not safely sanitize]"
        );
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldRedactValueWhenQueryParameterNameIsMalformed() {
        String result = ApiBaseTest.sanitizeQueryParameters(
                "%ZZ=private-value&page=2"
        );

        Assert.assertEquals(
                result,
                "%ZZ=[REDACTED]&page=2"
        );

        Assert.assertFalse(result.contains("private-value"));
    }

    @Test(groups = {"UNIT", "REGRESSION"})
    public void shouldOmitUserInformationAndFragmentFromUri() {
        String result = ApiBaseTest.sanitizeRequestUri(
                "https://user:password@api.example.test/users/1"
                        + "?page=2#private-section"
        );

        Assert.assertEquals(
                result,
                "https://api.example.test/users/1?page=2"
        );

        Assert.assertFalse(result.contains("password"));
        Assert.assertFalse(result.contains("private-section"));
    }
}
