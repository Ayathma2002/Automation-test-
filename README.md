# SauceDemo Automation Framework

Selenium + TestNG + Page Object Model automation for [https://www.saucedemo.com/](https://www.saucedemo.com/).

## Automated scenarios

1. User Login — `LoginTest`
2. Sort Products (replaces Search; SauceDemo has no search) — `SortProductsTest`
3. Add Product to Cart — `AddToCartTest`
4. Update Shopping Cart (remove item) — `UpdateCartTest`
5. User Logout — `LogoutTest`

## Prerequisites

- Java 17+
- Maven
- Google Chrome installed

## How to run tests

### 1. Configure credentials (`.env`)

Credentials are **not** stored in the repo. Use the example file as a template:

```bash
cp .env.example .env
```

Open `.env` and replace the placeholders with your valid SauceDemo login:

```env
VALID_USERNAME=your_username
VALID_PASSWORD=your_password
```

Example for the public demo site ([saucedemo.com](https://www.saucedemo.com/)):

| File | Purpose | Committed to Git? |
|------|---------|-------------------|
| `.env.example` | Template with placeholders | Yes |
| `.env` | Your real credentials | No (gitignored) |

### 2. Run the full suite

From the project root:

```bash
mvn test
```

Expected result:

```text
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### 3. Run a single test class (optional)

```bash
mvn -Dtest=LoginTest test
mvn -Dtest=AddToCartTest test
```

### Notes

- `VALID_USERNAME` / `VALID_PASSWORD` are read from `.env` (or OS environment variables), not from `config.properties`.
- Non-secret settings (URL, browser, waits) are in `src/test/resources/config.properties`.
- Expected assertion values (titles, sort, cart counts) are in `src/test/resources/testdata/products.json` via `TestData`.

## Documentation

| Document | Assignment step |
|----------|-----------------|
| [docs/ASSIGNMENT_REPORT.md](docs/ASSIGNMENT_REPORT.md) | Full PDF report draft (Steps 1–10) |
| [docs/FRAMEWORK.md](docs/FRAMEWORK.md) | Step 5 – Framework design / packages |
| [docs/DEBUGGING_CHALLENGE.md](docs/DEBUGGING_CHALLENGE.md) | Step 7 – Debugging challenge |
| [AI_USAGE.md](AI_USAGE.md) | AI assistance disclosure |

## Package overview

- `com.saucedemo.base` — WebDriver setup/teardown (`BaseTest`)
- `com.saucedemo.pages` — Page objects (`LoginPage`, `InventoryPage`, `CartPage`)
- `com.saucedemo.tests` — TestNG test classes
- `com.saucedemo.utils` — `ConfigReader` (`.env` + properties) and `TestData` (JSON fixtures)
