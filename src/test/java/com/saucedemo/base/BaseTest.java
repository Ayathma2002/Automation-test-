package com.saucedemo.base;

import com.saucedemo.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseTest {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    protected WebDriver getDriver() {
        return DRIVER.get();
    }

    @BeforeMethod
    public void setup() {
        String browser = ConfigReader.get("browser").toLowerCase();
        if (!"chrome".equals(browser)) {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-notifications");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        WebDriver driver = new ChromeDriver(options);
        DRIVER.set(driver);
        driver.get(ConfigReader.get("base.url"));
        new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds")))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            int pauseSeconds = ConfigReader.getInt("pause.before.quit.seconds", 0);
            if (pauseSeconds > 0) {
                try {
                    Thread.sleep(pauseSeconds * 1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            driver.quit();
            DRIVER.remove();
        }
    }
}
