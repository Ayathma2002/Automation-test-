package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class InventoryPage extends BasePage {

    private static final By INVENTORY_LIST = By.cssSelector(".inventory_list");
    private static final By PAGE_TITLE = By.cssSelector(".title");
    private static final By SORT_DROPDOWN = By.cssSelector("[data-test='product-sort-container']");
    private static final By PRODUCT_NAMES = By.cssSelector(".inventory_item_name");
    private static final By PRODUCT_PRICES = By.cssSelector(".inventory_item_price");
    private static final By CART_BADGE = By.cssSelector(".shopping_cart_badge");
    private static final By CART_LINK = By.cssSelector(".shopping_cart_link");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By LOGOUT_LINK = By.id("logout_sidebar_link");
    private static final By ADD_BACKPACK = By.id("add-to-cart-sauce-labs-backpack");
    private static final By ADD_BIKE_LIGHT = By.id("add-to-cart-sauce-labs-bike-light");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isInventoryDisplayed() {
        return isDisplayed(INVENTORY_LIST);
    }

    public String getInventoryTitle() {
        return getText(PAGE_TITLE);
    }

    public InventoryPage sortBy(String visibleOptionText) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(SORT_DROPDOWN));
        new Select(dropdown).selectByVisibleText(visibleOptionText);
        return this;
    }

    public String getFirstProductName() {
        List<WebElement> names = findAll(PRODUCT_NAMES);
        return names.get(0).getText();
    }

    public String getFirstProductPrice() {
        List<WebElement> prices = findAll(PRODUCT_PRICES);
        return prices.get(0).getText();
    }

    public InventoryPage addBackpackToCart() {
        click(ADD_BACKPACK);
        wait.until(ExpectedConditions.visibilityOfElementLocated(CART_BADGE));
        return this;
    }

    public InventoryPage addBikeLightToCart() {
        click(ADD_BIKE_LIGHT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(CART_BADGE));
        return this;
    }

    public String getCartBadgeCount() {
        return getText(CART_BADGE);
    }

    public boolean isCartBadgeVisible() {
        return !driver.findElements(CART_BADGE).isEmpty()
                && driver.findElement(CART_BADGE).isDisplayed();
    }

    public CartPage openCart() {
        click(CART_LINK);
        wait.until(ExpectedConditions.urlContains("cart"));
        return new CartPage(driver);
    }

    public LoginPage logout() {
        click(MENU_BUTTON);
        WebElement logoutLink = wait.until(ExpectedConditions.visibilityOfElementLocated(LOGOUT_LINK));
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
        jsClick(logoutLink);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("inventory")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        return new LoginPage(driver);
    }
}
