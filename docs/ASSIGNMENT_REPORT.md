# SauceDemo UI Test Automation Assignment Report

**Application under test:** [https://www.saucedemo.com/](https://www.saucedemo.com/) (Swag Labs)  
**Tech stack:** Java, Selenium WebDriver, TestNG, Maven, Page Object Model  
**Project artifact:** `saucedemo-automation` (`com.saucedemo`)

> Convert this document to PDF for submission (Word / Google Docs / Pandoc).  
> Insert your own screenshots where marked **[Screenshot]**.

---

## Table of Contents

1. Step 1 – Website Selection  
2. Step 2 – Requirement Analysis  
3. Step 3 – Manual Test Scenario Design  
4. Step 4 – Automation Decision  
5. Step 5 – Framework Design  
6. Step 6 – Automation Development  
7. Step 7 – Debugging Challenge  
8. Step 8 – Reflection Video Outline  
9. Step 9 – Viva Preparation Notes  
10. Step 10 – GitHub Repository  
11. Conclusion  

---

## Step 1 – Website Selection (5 Marks)

### Selected website

| Item | Detail |
|------|--------|
| Website | Sauce Demo / Swag Labs |
| URL | https://www.saucedemo.com/ |
| Type | Public demo e-commerce front-end |
| Why chosen | Stable, purpose-built for automation practice; clear login → inventory → cart → checkout flows; publicly accessible without registration |

### Scope of testing

Functional UI testing of authentication, product browsing/sorting, shopping cart, and logout. Checkout beyond cart update was considered out of automation scope for this assignment’s five-scenario limit, but is covered in manual scenarios.

**[Screenshot]** SauceDemo login page (home page)

---

## Step 2 – Requirement Analysis (10 Marks)

### 2.1 Objectives

1. Validate that a standard user can authenticate and reach the product inventory.  
2. Verify product listing behaviour (display, sorting).  
3. Verify cart operations: add item(s), view cart, remove item.  
4. Verify session end via logout returns the user to the login page.  
5. Build a maintainable Selenium + TestNG + POM framework that automates five high-value scenarios.  
6. Demonstrate debugging skill via an intentional failure, root-cause analysis, and fix.

### 2.2 Users

| User type | Description | Credentials (demo) |
|-----------|-------------|--------------------|
| Standard shopper | Can browse, sort, cart, checkout | `standard_user` / `secret_sauce` |
| Locked-out user | Cannot log in | `locked_out_user` / `secret_sauce` |
| Problem user | UI/data quirks for negative testing | `problem_user` / `secret_sauce` |
| Performance glitch user | Slow responses | `performance_glitch_user` / `secret_sauce` |

Primary test focus for automation: **standard_user**.

### 2.3 Key features

| Feature | Description |
|---------|-------------|
| Login | Username + password authentication |
| Inventory | Product cards with name, description, price, add-to-cart |
| Product sort | Sort by name (A–Z / Z–A) and price (low–high / high–low) |
| Shopping cart | Badge count, cart page, remove items |
| Burger menu | All Items, About, Logout, Reset App State |
| Checkout | Information → Overview → Complete (manual coverage) |

**Note:** SauceDemo does **not** provide a product search box. Sorting and browsing replace “search” for realistic coverage.

### 2.4 Assumptions

1. Demo site remains publicly available at the current URL during assessment.  
2. Demo credentials remain unchanged (`standard_user` / `secret_sauce`).  
3. Tests run on Google Chrome with a compatible ChromeDriver (managed via WebDriverManager).  
4. Network connectivity is available for loading the site.  
5. Cart quantity cannot be edited numerically; “update cart” means removing items.  
6. Each automated test starts from a clean browser session (`@BeforeMethod` / `@AfterMethod`).

### 2.5 Risks

| Risk | Impact | Mitigation |
|------|--------|------------|
| Site downtime / URL change | Blocked testing | Document URL; keep screenshots of prior runs |
| Credential or UI change | Broken locators/tests | Externalize config; use stable `id` / `data-test` locators |
| Flaky React inputs / waits | Intermittent failures | Explicit waits; robust login input handling |
| Browser/driver mismatch | Suite fails to launch | WebDriverManager auto-resolves driver |
| Demo data resets | Shared state surprises | Fresh browser per method; avoid depending on leftover cart |

### 2.6 Observations

1. Application is a single-page-app style front-end with clear, testable element ids.  
2. Login error messages are descriptive (e.g. locked-out user).  
3. Cart badge updates immediately after add/remove — good assertion points.  
4. No native search; product discovery is via list + sort.  
5. Suitable for teaching POM because screens map cleanly to page classes.  
6. Early project naming used `nopcommerce`; packages were renamed to `com.saucedemo` to match the AUT.

---

## Step 3 – Manual Test Scenario Design (15 Marks)

Priority legend: **P1** = High / Critical, **P2** = Medium, **P3** = Low.

| ID | Scenario | Steps (summary) | Priority | Expected result | Screenshot |
|----|----------|-----------------|----------|-----------------|------------|
| TS-01 | Valid login | Open site → enter `standard_user` / `secret_sauce` → Login | P1 | Redirect to inventory; title “Products” | **[Screenshot]** |
| TS-02 | Invalid password | Valid username + wrong password → Login | P1 | Error: credentials do not match | **[Screenshot]** |
| TS-03 | Empty username | Leave username blank → Login | P1 | Error: Username is required | **[Screenshot]** |
| TS-04 | Empty password | Enter username only → Login | P1 | Error: Password is required | **[Screenshot]** |
| TS-05 | Locked-out user | Login as `locked_out_user` | P1 | Error: user has been locked out | **[Screenshot]** |
| TS-06 | Inventory loads | After valid login, inspect product list | P1 | Multiple products with name and price visible | **[Screenshot]** |
| TS-07 | Sort price low–high | Open sort → Price (low to high) | P2 | First product is cheapest (Onesie $7.99) | **[Screenshot]** |
| TS-08 | Sort name Z–A | Open sort → Name (Z to A) | P2 | Products ordered descending by name | Optional |
| TS-09 | Open product details | Click a product name | P2 | Product detail page shows matching name/price | Optional |
| TS-10 | Add one item to cart | Click Add to cart on Backpack | P1 | Button becomes Remove; badge shows 1 | **[Screenshot]** |
| TS-11 | Add multiple items | Add Backpack + Bike Light | P1 | Badge shows 2 | **[Screenshot]** |
| TS-12 | View cart | Click cart icon | P1 | Cart page lists added items | **[Screenshot]** |
| TS-13 | Remove item from cart | On cart, click Remove | P1 | Item removed; badge decrements | **[Screenshot]** |
| TS-14 | Continue shopping | From cart → Continue Shopping | P2 | Returns to inventory | Optional |
| TS-15 | Checkout happy path | Cart → Checkout → fill info → Continue → Finish | P1 | Order complete confirmation shown | **[Screenshot]** |
| TS-16 | Checkout missing postal code | Checkout with empty postal code | P2 | Validation error for postal code | Optional |
| TS-17 | Logout | Open menu → Logout | P1 | Returned to login page | **[Screenshot]** |
| TS-18 | Reset App State | Add items → Menu → Reset App State | P3 | Cart cleared / state reset | Optional |

**Minimum requirement met:** 18 scenarios (≥ 15).

---

## Step 4 – Automation Decision (10 Marks)

### 4.1 Scenarios selected for automation

| # | Automated scenario | Manual ID | Test class |
|---|--------------------|-----------|------------|
| 1 | User Login | TS-01 | `LoginTest` |
| 2 | Sort Products (low to high) | TS-07 | `SortProductsTest` |
| 3 | Add Product to Cart | TS-10 / TS-12 | `AddToCartTest` |
| 4 | Update Shopping Cart (remove) | TS-13 | `UpdateCartTest` |
| 5 | User Logout | TS-17 | `LogoutTest` |

Originally “Product Search” was considered; SauceDemo has **no search**. It was replaced with **Sort Products**, which is a real, automatable catalog feature.

### 4.2 Why these five were automated

1. **High business criticality (P1/P2)** — login, cart, and logout are core user journeys.  
2. **Stable, repeatable UI** — clear locators (`id`, `data-test`) reduce maintenance cost.  
3. **Strong ROI** — run frequently after any change; catch regressions quickly.  
4. **Fit for POM** — each maps cleanly to page objects and assertions.  
5. **Independent / isolatable** — each test can log in fresh and assert a focused outcome.  
6. **Sort replaces search** — still validates catalog interaction without inventing a non-existent feature.

### 4.3 Why the others were not automated (yet)

| Manual ID | Reason not automated |
|-----------|----------------------|
| TS-02–TS-05 | Negative login cases — valuable but lower priority than happy-path suite for this assignment’s five-test limit |
| TS-08–TS-09 | Additional browse variants; one sort case sufficiently proves sort automation |
| TS-11 | Partially covered by Update Cart (adds two items before remove) |
| TS-14 | Simple navigation; lower risk than add/remove |
| TS-15–TS-16 | Checkout is multi-step and form-heavy; better as a follow-up sprint |
| TS-18 | Reset is a utility action; less critical than primary shopper path |

**Selection principle:** Automate stable, high-frequency, high-risk paths first; keep exploratory / edge / long flows manual for this submission.

---

## Step 5 – Framework Design (15 Marks)

### 5.1 Architecture

```
Selenium WebDriver + TestNG + Maven + Page Object Model
```

```
src/test/java/com/saucedemo/
├── base/      → BaseTest (driver lifecycle)
├── pages/     → BasePage, LoginPage, InventoryPage, CartPage
├── tests/     → five scenario test classes
└── utils/     → ConfigReader, TestData

src/test/resources/
├── config.properties          # URL, browser, waits
├── testng.xml
└── testdata/products.json     # Expected assertion values

.env / .env.example            # Credentials (secret vs template)
```

### 5.2 Package explanations

#### `com.saucedemo.base`
Owns browser lifecycle. `BaseTest` starts Chrome before each method, opens `base.url`, and quits afterward. Uses `ThreadLocal<WebDriver>` so the design can scale to parallel runs later.

#### `com.saucedemo.pages`
POM layer. Locators and UI actions live here — not in test classes.

| Class | Role |
|-------|------|
| `BasePage` | Shared wait/click/type helpers |
| `LoginPage` | Login form interactions |
| `InventoryPage` | Products, sort, add-to-cart, menu logout |
| `CartPage` | Cart list, remove item, continue shopping |

#### `com.saucedemo.tests`
TestNG classes with assertions only. They call page methods and verify expected outcomes.

#### `com.saucedemo.utils`
| Class | Role |
|-------|------|
| `ConfigReader` | Non-secret settings from `config.properties`; credentials from `.env` |
| `TestData` | JSON fixtures from `src/test/resources/testdata/` (e.g. `products.json`) |

### 5.3 Design decisions

1. POM separates UI from tests → easier maintenance.  
2. Explicit waits preferred over implicit waits.  
3. Fluent methods return the next page object for readable flows.  
4. **Three data layers:** `.env` (secrets), `config.properties` (environment), `testdata/*.json` (expected UI values).  
5. Package naming aligned to SauceDemo (`com.saucedemo`), not the original scaffold name.

**[Screenshot]** Project package structure in IDE  
**[Screenshot]** `testng.xml` suite configuration  

*(Full write-up also available in repository: `docs/FRAMEWORK.md`.)*

---

## Step 6 – Automation Development (20 Marks)

### 6.1 Implementation summary

| Scenario | Key actions automated | Main assertions |
|----------|----------------------|-----------------|
| Login | Enter credentials → submit | Inventory visible; title from `products.json`; URL contains `inventory` |
| Sort | Login → sort using option from JSON | First product name/price from `products.json` |
| Add to cart | Login → add Backpack → open cart | Badge/item counts from JSON |
| Update cart | Add two items → remove Backpack | Counts after remove from JSON |
| Logout | Login → burger menu → Logout | Login page loaded; URL not inventory |

### 6.2 How to run

```bash
mvn test
```

**Expected result:** `Tests run: 5, Failures: 0, Errors: 0, Skipped: 0`

**[Screenshot]** Terminal BUILD SUCCESS  
**[Screenshot]** TestNG / Surefire report (optional)

### 6.3 Sample flow (Add to Cart)

1. `BaseTest.setup()` opens SauceDemo.  
2. `LoginPage.login(...)` authenticates.  
3. `InventoryPage.addBackpackToCart()` clicks Add to cart.  
4. Assert cart badge = `1`.  
5. `openCart()` → assert cart contents.  
6. `BaseTest.tearDown()` closes browser.

---

## Step 7 – Debugging Challenge (10 Marks)

### 7.1 Intentional defect

In `InventoryPage`, the Add to Cart locator was deliberately broken:

```java
// Broken
By.id("add-to-cart-sauce-labs-backpack-WRONG")

// Fixed
By.id("add-to-cart-sauce-labs-backpack")
```

### 7.2 Failure symptom

```text
TimeoutException: waiting for element to be clickable:
By.id: add-to-cart-sauce-labs-backpack-WRONG
```

**[Screenshot]** Failed test / Surefire output  

Evidence file: `docs/debugging/intentional-failure-output.txt`

### 7.3 Root cause

Incorrect locator string — no matching DOM element. Application behaviour was fine; automation was wrong.

### 7.4 Debugging steps

1. Inspect stack trace → `AddToCartTest` → `addBackpackToCart()`.  
2. Use Chrome DevTools on inventory page.  
3. Confirm real button id.  
4. Correct page-object constant.  
5. Re-run suite → all five tests pass.

**[Screenshot]** DevTools element inspection  
**[Screenshot]** Passing run after fix  

*(Full narrative: `docs/DEBUGGING_CHALLENGE.md`.)*

---

## Step 8 – Reflection Video Outline (10 Marks)

Use this script for a ~5-minute screen recording:

| Time | Talk about | Show on screen |
|------|------------|----------------|
| 0:00–0:40 | Website choice + objectives | SauceDemo login page |
| 0:40–1:20 | Requirement analysis highlights | Report Step 2 (or slides) |
| 1:20–2:00 | Manual vs automated selection | Scenario table / Step 4 |
| 2:00–3:10 | Framework packages + POM | IDE package tree + one page class |
| 3:10–4:10 | Run automated suite | `mvn test` success |
| 4:10–4:45 | Debugging challenge | Wrong locator → fix → pass |
| 4:45–5:00 | Lessons learned | Short closing |

**Lessons learned (say aloud):**
- POM keeps tests readable and locators maintainable.  
- Explicit waits make failures clearer.  
- Always verify locators in DevTools.  
- Automate critical paths first; keep edge cases manual when time-boxed.

---

## Step 9 – Viva Preparation Notes (5 Marks)

Be ready to answer:

1. **Why POM?** Separates UI locators/actions from assertions; one UI change → one page fix.  
2. **Why TestNG?** Annotations, suite XML, clear reports, easy Maven integration.  
3. **Why not automate all 15+?** Time/ROI; focus on stable, high-priority journeys.  
4. **Why sort instead of search?** Site has no search feature.  
5. **What is `BaseTest` for?** Driver setup/teardown per method.  
6. **Explicit vs implicit wait?** Explicit waits for specific conditions; more precise and less flaky.  
7. **How did you debug?** Stack trace → DevTools → fix locator → re-run.  
8. **Where are credentials stored?** In a gitignored `.env` file (`VALID_USERNAME` / `VALID_PASSWORD`), loaded by `ConfigReader`. Non-secret settings stay in `config.properties`. Expected assertion values (titles, prices, cart counts) are in `testdata/products.json` via `TestData`.
9. **Why JSON test data?** Keeps expected values out of test code so data can change without editing Java classes (similar idea to Cypress fixtures).

---

## Step 10 – GitHub Repository (10 Marks)

### Recommended commit history (meaningful messages)

1. Initial Maven + TestNG + Selenium framework setup  
2. Implement login page object and login test  
3. Add inventory sort automation  
4. Add cart page and add-to-cart / update-cart tests  
5. Add logout automation and TestNG suite wiring  
6. Document intentional failure and locator fix (debugging challenge)  
7. Add assignment report, framework docs, and AI_USAGE.md  

### Submission checklist

- [ ] GitHub repository link  
- [ ] Project ZIP  
- [ ] This report as PDF (with screenshots inserted)  
- [ ] 5-minute reflection video  
- [ ] `AI_USAGE.md`  

---

## Conclusion

This project delivers a complete SauceDemo UI automation solution using Java, Selenium, TestNG, and the Page Object Model. Five critical scenarios were automated, supported by requirement analysis, eighteen manual scenarios, a clear automation decision, framework documentation, and a demonstrated debugging cycle from intentional failure to fix.

---

## Appendix A – Test credentials (demo only)

| Username | Password | Notes |
|----------|----------|-------|
| standard_user | secret_sauce | Used in automation |
| locked_out_user | secret_sauce | Manual negative test |
| problem_user | secret_sauce | Manual exploration |
| performance_glitch_user | secret_sauce | Manual exploration |

## Appendix B – Key repository paths

| Path | Purpose |
|------|---------|
| `src/test/java/com/saucedemo/` | Framework source |
| `src/test/resources/config.properties` | Non-secret settings (URL, browser, waits) |
| `.env` (gitignored) / `.env.example` | Login credentials |
| `src/test/resources/testdata/products.json` | Expected product/cart assertion data |
| `src/test/java/.../utils/TestData.java` | JSON fixture loader |
| `src/test/resources/testng.xml` | Suite definition |
| `docs/FRAMEWORK.md` | Framework detail |
| `docs/DEBUGGING_CHALLENGE.md` | Step 7 detail |
| `AI_USAGE.md` | AI assistance disclosure |
