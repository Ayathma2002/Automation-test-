package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Replaces "Product Search" (SauceDemo has no search).
 * Validates product sorting as a core catalog interaction.
 */
public class SortProductsTest extends BaseTest {

    @Test(description = "Scenario 2: User can sort products by price low to high")
    public void verifyUserCanSortProductsByPriceLowToHigh() {
        InventoryPage inventoryPage = new LoginPage(getDriver()).login(
                ConfigReader.get("valid.username"),
                ConfigReader.get("valid.password")
        );

        inventoryPage.sortBy(TestData.getString("products.json", "sort", "priceLowToHigh", "optionText"));

        Assert.assertEquals(inventoryPage.getFirstProductName(),
                TestData.getString("products.json", "sort", "priceLowToHigh", "firstProductName"),
                "Cheapest product should appear first after low-to-high sort");
        Assert.assertEquals(inventoryPage.getFirstProductPrice(),
                TestData.getString("products.json", "sort", "priceLowToHigh", "firstProductPrice"));
    }
}
