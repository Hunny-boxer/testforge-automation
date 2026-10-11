
package com.hunny.testforge.tests;

import com.hunny.testforge.base.BaseTest;
import com.hunny.testforge.pages.GooglePage;
import com.hunny.testforge.reporting.ExtentTestListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ExtentTestListener.class)
public class GoogleSearchRegressionTest extends BaseTest {

    @Test(groups = {"UI", "REGRESSION"})
    public void googleHomePageTitleShouldContainGoogle() {

        GooglePage googlePage = new GooglePage(driver);
        googlePage.open();

        String title = googlePage.getPageTitle();

        Assert.assertTrue(
                title != null
                        && title.toLowerCase().contains("google"),
                "Google homepage title should contain 'Google'. Actual title: "
                        + title
        );
    }
}
