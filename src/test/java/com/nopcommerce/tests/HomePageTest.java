package com.nopcommerce.tests;

import com.nopcommerce.base.BaseTest;
import com.nopcommerce.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomePageTest extends BaseTest {

    @Test
    public void verifyHomePageLoads() throws InterruptedException {
        HomePage homePage = new HomePage(driver);
        String title = homePage.getPageTitle();
        String currentUrl = driver.getCurrentUrl();

        System.out.println("Home page title: " + title);
        System.out.println("Home page URL: " + currentUrl);

        Assert.assertTrue(currentUrl.contains("saucedemo.com"), "The browser should navigate to the Sauce Demo homepage URL");
        Assert.assertTrue(title.contains("Swag Labs"), "The home page title should contain 'Swag Labs'");

        // Pause so you can see the browser before it closes
        Thread.sleep(5000);
    }

}