package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * SauceDemo cart does not support quantity edits; "update" is implemented as remove item.
 */
public class UpdateCartTest extends BaseTest {

    @Test(description = "Scenario 4: User can update the cart by removing a product")
    public void verifyUserCanUpdateCartByRemovingItem() {
        InventoryPage inventoryPage = new LoginPage(getDriver()).login(
                ConfigReader.get("valid.username"),
                ConfigReader.get("valid.password")
        );

        inventoryPage.addBackpackToCart().addBikeLightToCart();
        Assert.assertEquals(inventoryPage.getCartBadgeCount(),
                TestData.getString("products.json", "cart", "afterTwoAdds", "badgeCount"));

        CartPage cartPage = inventoryPage.openCart();
        Assert.assertEquals(cartPage.getCartItemCount(),
                TestData.getInt("products.json", "cart", "afterTwoAdds", "itemCount"));

        cartPage.removeBackpack();

        Assert.assertEquals(cartPage.getCartItemCount(),
                TestData.getInt("products.json", "cart", "afterOneRemove", "itemCount"),
                "Cart should have one item left after remove");
        Assert.assertEquals(cartPage.getCartBadgeCount(),
                TestData.getString("products.json", "cart", "afterOneRemove", "badgeCount"),
                "Cart badge should update to 1 after remove");
    }
}
