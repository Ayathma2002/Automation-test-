# Framework Design (Step 5)

Selenium + TestNG + Page Object Model (POM) automation framework for [SauceDemo](https://www.saucedemo.com/).

## Technology stack

| Tool | Role |
|------|------|
| Java 17 | Language |
| Maven | Build and dependency management |
| Selenium WebDriver 4 | Browser automation |
| TestNG | Test runner, assertions, suite config |
| WebDriverManager | ChromeDriver setup |
| dotenv-java | Loads credentials from `.env` |
| Jackson | Loads JSON test fixtures |
| Page Object Model | Separates UI locators/actions from tests |

## Package structure

```
src/
├── main/java/com/saucedemo/
│   └── App.java
└── test/
    ├── java/com/saucedemo/
    │   ├── base/                # Test lifecycle / WebDriver setup
    │   ├── pages/               # Page Object classes
    │   ├── tests/               # TestNG test classes
    │   └── utils/               # ConfigReader + TestData
    └── resources/
        ├── config.properties    # URL, browser, waits (non-secret)
        ├── testng.xml
        └── testdata/
            └── products.json    # Expected UI values / assertion data
.env                             # Credentials (gitignored)
.env.example                     # Credential placeholders (committed)
```

### `com.saucedemo.base`

Contains `BaseTest`, which owns the WebDriver lifecycle:

- `@BeforeMethod` — starts Chrome, opens the base URL
- `@AfterMethod` — quits the browser
- `ThreadLocal<WebDriver>` — isolates drivers if tests run in parallel later

All test classes extend `BaseTest` so setup/teardown is not duplicated.

### `com.saucedemo.pages`

Page Object Model layer. Each class maps to a UI screen and exposes business actions, not raw Selenium calls from tests.

| Class | Responsibility |
|-------|----------------|
| `BasePage` | Shared waits, click/type/getText helpers |
| `LoginPage` | Username/password entry, login, login-page readiness |
| `InventoryPage` | Product list, sort, add-to-cart, cart badge, logout menu |
| `CartPage` | Cart contents, remove item, continue shopping |

### `com.saucedemo.tests`

Executable TestNG scenarios (the five selected for automation):

| Class | Scenario |
|-------|----------|
| `LoginTest` | User login |
| `SortProductsTest` | Sort products (replaces search — SauceDemo has no search) |
| `AddToCartTest` | Add product to cart |
| `UpdateCartTest` | Update cart by removing an item |
| `LogoutTest` | User logout |

### `com.saucedemo.utils`

| Class | Role |
|-------|------|
| `ConfigReader` | Reads non-secret settings from `config.properties` and credentials from `.env` / OS env |
| `TestData` | Loads JSON fixtures from `src/test/resources/testdata/` (similar to Cypress `cy.fixture()`) |

### Data layers (important)

| Source | What it stores | Example |
|--------|----------------|---------|
| `.env` (gitignored) | Secrets / login credentials | `VALID_USERNAME`, `VALID_PASSWORD` |
| `config.properties` | Environment settings | `base.url`, `browser`, waits |
| `testdata/*.json` | Expected test values for assertions | sort option, product name/price, cart counts |

Tests should not hard-code expected titles, prices, or badge counts — those come from `TestData` + `products.json`.

## Design decisions

1. **POM** — locators live in page classes; tests only assert outcomes.
2. **Explicit waits in `BasePage`** — preferred over implicit waits for stability.
3. **Fluent page methods** — methods return the next page object for readable flows.
4. **Separated config vs secrets vs fixtures** — `.env` for credentials, properties for env settings, JSON for assertion data.
5. **Package naming** — `com.saucedemo` matches the application under test.
