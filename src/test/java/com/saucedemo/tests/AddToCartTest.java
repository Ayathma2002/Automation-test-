package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AddToCartTest extends BaseTest {

    @Test(description = "Scenario 3: User can add a product to the shopping cart")
    public void verifyUserCanAddProductToCart() {
        InventoryPage inventoryPage = new LoginPage(getDriver()).login(
                ConfigReader.get("valid.username"),
                ConfigReader.get("valid.password")
        );

        inventoryPage.addBackpackToCart();

        Assert.assertTrue(inventoryPage.isCartBadgeVisible(), "Cart badge should appear after add");
        Assert.assertEquals(inventoryPage.getCartBadgeCount(),
                TestData.getString("products.json", "cart", "afterOneAdd", "badgeCount"));

        CartPage cartPage = inventoryPage.openCart();
        Assert.assertEquals(cartPage.getCartTitle(),
                TestData.getString("products.json", "cartTitle"));
        Assert.assertEquals(cartPage.getCartItemCount(),
                TestData.getInt("products.json", "cart", "afterOneAdd", "itemCount"),
                "Cart should contain exactly one product");
    }
}
