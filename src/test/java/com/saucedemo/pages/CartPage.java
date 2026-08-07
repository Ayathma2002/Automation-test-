package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage {

    private static final By PAGE_TITLE = By.cssSelector(".title");
    private static final By CART_ITEMS = By.cssSelector(".cart_item");
    private static final By CART_BADGE = By.cssSelector(".shopping_cart_badge");
    private static final By REMOVE_BACKPACK = By.id("remove-sauce-labs-backpack");
    private static final By CONTINUE_SHOPPING = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public String getCartTitle() {
        return getText(PAGE_TITLE);
    }

    public int getCartItemCount() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart_list")));
        return driver.findElements(CART_ITEMS).size();
    }

    public CartPage removeBackpack() {
        click(REMOVE_BACKPACK);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(REMOVE_BACKPACK));
        return this;
    }

    public boolean isCartBadgeVisible() {
        return !driver.findElements(CART_BADGE).isEmpty()
                && driver.findElement(CART_BADGE).isDisplayed();
    }

    public String getCartBadgeCount() {
        return getText(CART_BADGE);
    }

    public InventoryPage continueShopping() {
        click(CONTINUE_SHOPPING);
        wait.until(ExpectedConditions.urlContains("inventory"));
        return new InventoryPage(driver);
    }
}
