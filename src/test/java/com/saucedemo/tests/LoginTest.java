package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Scenario 1: User can log in with valid credentials")
    public void verifyUserCanLogin() {
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login(
                ConfigReader.get("valid.username"),
                ConfigReader.get("valid.password")
        );

        Assert.assertTrue(inventoryPage.isInventoryDisplayed(),
                "Inventory page should be visible after successful login");
        Assert.assertEquals(inventoryPage.getInventoryTitle(),
                TestData.getString("products.json", "inventoryTitle"));
        Assert.assertTrue(inventoryPage.getCurrentUrl().contains("inventory.html"));
    }
}
