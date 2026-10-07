package com.hunny.testforge.tests;

import com.hunny.testforge.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GoogleSearchTest extends BaseTest {

    @Test
    public void openGoogle() {

        driver.get("https://www.google.com");

        String title = driver.getTitle();

        System.out.println("Page title: " + title);

        Assert.assertTrue(
                title != null && !title.isEmpty(),
                "Page title should not be empty"
        );
    }
}
