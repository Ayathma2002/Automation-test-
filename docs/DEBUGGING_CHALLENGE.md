# Step 7 – Debugging Challenge

## Intentional failure

**Scenario affected:** Add Product to Cart (`AddToCartTest`)

**Change introduced:** Incorrect locator in `InventoryPage`

```java
// Broken (intentional)
private static final By ADD_BACKPACK = By.id("add-to-cart-sauce-labs-backpack-WRONG");

// Correct (after fix)
private static final By ADD_BACKPACK = By.id("add-to-cart-sauce-labs-backpack");
```

## Observed failure

Running:

```bash
mvn -Dtest=AddToCartTest test
```

produced:

```text
org.openqa.selenium.TimeoutException:
Expected condition failed: waiting for element to be clickable:
By.id: add-to-cart-sauce-labs-backpack-WRONG
(tried for 10 second(s) with 500 milliseconds interval)
```

Full console evidence is saved in `docs/debugging/intentional-failure-output.txt`.

## Root cause

| Layer | Finding |
|-------|---------|
| Symptom | Test timed out waiting for the Add to Cart button |
| Stack trace | Failure at `InventoryPage.addBackpackToCart()` → `BasePage.click()` |
| Root cause | Locator id did not match any element on the inventory page |
| Why it failed this way | Explicit wait kept polling for 10 seconds, then threw `TimeoutException` |

This was **not** an application bug. The product page loaded correctly after login; only the automation locator was wrong.

## Debugging process

1. Read the Surefire / TestNG stack trace and identify the failing line (`AddToCartTest` → `addBackpackToCart`).
2. Open Chrome DevTools on [saucedemo.com](https://www.saucedemo.com/) inventory page (after login).
3. Inspect the Sauce Labs Backpack **Add to cart** button.
4. Confirm the real element id is `add-to-cart-sauce-labs-backpack`.
5. Compare with the page object constant and spot the `-WRONG` suffix.
6. Correct the locator and re-run the test.

## Fix demonstration

After restoring the correct locator:

```bash
mvn test
```

**Result:** `Tests run: 5, Failures: 0, Errors: 0, Skipped: 0` — BUILD SUCCESS

## Lesson learned

- Prefer stable attributes (`id`, `data-test`) and verify them in DevTools before coding.
- Explicit waits surface bad locators as clear timeouts instead of immediate `NoSuchElementException`.
- Keep page-object locators in one place (`InventoryPage`) so fixes are localized.
