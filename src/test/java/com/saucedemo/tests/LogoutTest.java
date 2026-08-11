package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LogoutTest extends BaseTest {

    @Test(description = "Scenario 5: User can log out and return to the login page")
    public void verifyUserCanLogout() {
        InventoryPage inventoryPage = new LoginPage(getDriver()).login(
                ConfigReader.get("valid.username"),
                ConfigReader.get("valid.password")
        );
        Assert.assertTrue(inventoryPage.isInventoryDisplayed());

        LoginPage loginPage = inventoryPage.logout();

        Assert.assertTrue(loginPage.isLoaded(),
                "Login page should be displayed after logout");
        Assert.assertFalse(loginPage.getCurrentUrl().contains("inventory"),
                "URL should no longer point to inventory after logout");
    }
}
