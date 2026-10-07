package com.hunny.testforge.tests;

import com.hunny.testforge.base.BaseTest;
import com.hunny.testforge.pages.GooglePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GoogleSearchTest extends BaseTest {

    @Test
    public void openGoogle() {

        GooglePage googlePage = new GooglePage(driver);

        googlePage.open();

        String title = googlePage.getPageTitle();

        System.out.println("Page title: " + title);

        Assert.assertTrue(
                title != null && !title.isEmpty(),
                "Page title should not be empty"
        );
    }
}
