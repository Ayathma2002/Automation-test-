package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(LOGIN_BUTTON)
                && getCurrentUrl().contains("saucedemo.com")
                && getPageTitle().contains("Swag Labs");
    }

    public LoginPage enterUsername(String username) {
        setInputValue(USERNAME, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        setInputValue(PASSWORD, password);
        return this;
    }

    /**
     * SauceDemo uses React controlled inputs. Native sendKeys is unreliable on some
     * Chrome/macOS combinations, so values are set via the native value setter + input event.
     */
    private void setInputValue(By locator, String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript(
                "const input = arguments[0];"
                        + "const value = arguments[1];"
                        + "const nativeInputValueSetter = "
                        + "Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;"
                        + "nativeInputValueSetter.call(input, value);"
                        + "input.dispatchEvent(new Event('input', { bubbles: true }));"
                        + "input.dispatchEvent(new Event('change', { bubbles: true }));",
                field,
                value
        );
        wait.until(ExpectedConditions.attributeToBe(locator, "value", value));
    }

    public InventoryPage clickLogin() {
        click(LOGIN_BUTTON);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("inventory"),
                ExpectedConditions.visibilityOfElementLocated(ERROR_MESSAGE)
        ));
        if (getCurrentUrl().contains("inventory")) {
            return new InventoryPage(driver);
        }
        throw new IllegalStateException("Login failed: " + getErrorMessage());
    }

    public InventoryPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLogin();
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }
}
