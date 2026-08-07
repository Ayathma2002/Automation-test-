# AI Usage Disclosure

**Project:** SauceDemo UI Test Automation (Selenium + TestNG + POM)  
**Tool used:** Cursor (AI coding assistant)  
**Purpose of this file:** Document how AI assisted the assignment, what was AI-generated vs student-owned, and how outputs were verified.

---

## 1. Summary

AI assistance was used as a **productivity and mentoring aid** for framework structure, missing automation scenarios, documentation drafts, and debugging guidance. All automated tests were executed locally with `mvn test`, and locator/behaviour decisions were validated against the live site [https://www.saucedemo.com/](https://www.saucedemo.com/).

AI did **not** replace understanding of Selenium, TestNG, or POM. Final code, report content, and technical decisions were reviewed and owned by the student.

---

## 2. Where AI was used

| Area | AI contribution | Student responsibility |
|------|-----------------|------------------------|
| Assignment gap analysis | Compared existing code to Steps 1–10; listed missing work | Confirmed scope and priorities |
| Package rename | Suggested renaming `nopcommerce` → `com.saucedemo` | Accepted rename to match AUT |
| Page Object Model | Helped draft/extend `LoginPage`, `InventoryPage`, `CartPage`, `BasePage`, `BaseTest` | Reviewed locators, waits, and assertions |
| Automated scenarios | Implemented Sort, Add to Cart, Update Cart, Logout tests | Chose final five scenarios; ran and verified suite |
| Search → Sort decision | Explained SauceDemo has no search; proposed Sort as replacement | Adopted Sort for Scenario 2 |
| Debugging challenge | Helped create intentional wrong locator, capture failure, document root cause/fix | Understood timeout failure and DevTools verification |
| Report drafting | Drafted PDF sections in `docs/ASSIGNMENT_REPORT.md` | Will insert screenshots, personalize, export PDF |
| `AI_USAGE.md` | Assisted drafting this disclosure | Confirmed accuracy of usage claims |

---

## 3. Where AI was not used (or used minimally)

- Lecturer approval of website selection (student decision)
- Recording the 5-minute reflection video (student)
- Viva answers (student preparation using notes)
- Creating/pushing the GitHub repository and commit messages (student, unless later assisted)
- Capturing final screenshots for the PDF (student)

---

## 4. Prompts / assistance themes (high level)

Examples of assistance requested (paraphrased):

1. Review current project against assignment marking scheme and list next steps.  
2. Complete Steps 5–7: rename packages, finish five automations, document debugging challenge.  
3. Draft PDF report sections and `AI_USAGE.md`.

No attempt was made to bypass academic integrity rules; this file exists to disclose AI use transparently.

---

## 5. Verification and quality control

After AI-assisted code changes, the following checks were performed:

1. **Compile / run:** `mvn test`  
2. **Result achieved:** `Tests run: 5, Failures: 0, Errors: 0, Skipped: 0`  
3. **Locator validation:** Compared page-object selectors with Chrome DevTools on SauceDemo  
4. **Debugging challenge:** Intentional bad locator produced `TimeoutException`; fix restored green suite  
5. **Documentation review:** Report draft aligned with actual packages, test names, and behaviour

Evidence artifacts:

- `docs/debugging/intentional-failure-output.txt`
- `docs/debugging/post-fix-pass-output.txt`
- `docs/DEBUGGING_CHALLENGE.md`

---

## 6. Limitations / corrections from AI output

| Issue encountered | Resolution |
|-------------------|------------|
| Initial idea of “Product Search” | Rejected — site has no search; replaced with Sort |
| Flaky login with native `sendKeys`/`clear` on React inputs | Adjusted login input strategy and re-tested until stable |
| Logout click reliability | Used wait + JS click for sidebar logout link |
| Scaffold package name `nopcommerce` | Renamed to `com.saucedemo` for clarity |

These corrections show AI suggestions were **critically reviewed**, not blindly accepted.

---

## 7. Academic integrity statement

I acknowledge that:

- AI tools were used to assist development and documentation.
- I understand the framework design, test scenarios, and debugging process.
- I can explain technical decisions in the viva.
- Submitted work reflects my project on SauceDemo automation, with AI assistance disclosed in this file.

**Student name:** ______________________________  
**Date:** ______________________________  
**GitHub repository:** ______________________________  

---

## 8. Related documents

| File | Content |
|------|---------|
| `docs/ASSIGNMENT_REPORT.md` | Full report draft for PDF export |
| `docs/FRAMEWORK.md` | Framework / package design |
| `docs/DEBUGGING_CHALLENGE.md` | Step 7 write-up |
| `README.md` | How to run the suite |
