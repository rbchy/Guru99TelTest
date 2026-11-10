<div align="center">

# 📞 Guru99 Telecom — Selenium Test Automation (Java · JUnit 5)

### End-to-end tests for the Guru99 Telecom demo: add a customer, create a tariff plan and assign it, with Page Objects, explicit waits and a live run on every push

[![Typing SVG](https://readme-typing-svg.demolab.com/?font=Fira+Code&size=18&pause=1500&color=0EA5E9&center=true&vCenter=true&width=900&repeat=true&lines=Java+17+%C2%B7+Selenium+4.27+%C2%B7+JUnit+5+%C2%B7+Maven;Add+customer+%E2%86%92+Add+tariff+%E2%86%92+Assign+plan;29+%2F+29+green+%C2%B7+live+site+in+headless+Chrome)](https://git.io/typing-svg)

<!-- ============ STATUS BAR ============ -->
[![CI](https://github.com/rbchy/Guru99TelTest/actions/workflows/ci.yml/badge.svg)](https://github.com/rbchy/Guru99TelTest/actions/workflows/ci.yml)
![Tests](https://img.shields.io/badge/Tests-29%20%2F%2029%20passed-22c55e?style=for-the-badge&logo=checkmarx&logoColor=white)
![Site](https://img.shields.io/badge/Target-demo.guru99.com%2Ftelecom-0ea5e9?style=for-the-badge)
![License](https://img.shields.io/badge/License-MPL%202.0-lightgrey?style=for-the-badge)

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-4.27-43B02A?style=for-the-badge&logo=selenium&logoColor=white)
![JUnit 5](https://img.shields.io/badge/JUnit%205-5.11-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-CI-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)

![Last Commit](https://img.shields.io/github/last-commit/rbchy/Guru99TelTest?style=flat-square&color=0ea5e9&label=Last%20commit)
![Repo Size](https://img.shields.io/github/repo-size/rbchy/Guru99TelTest?style=flat-square&color=155e75&label=Repo%20size)

</div>

---

## 📌 At a Glance

<table>
<tr>
<td width="50%" valign="top">

**🙋 What this is**

A small, complete Selenium framework for the **[Guru99 Telecom demo](https://demo.guru99.com/telecom/)**, a practice site for a telecom provider's back office. It walks through the core business flow the way a support agent would:

1. register a customer;
2. create a tariff plan;
3. assign that plan to the customer.

It also includes two negative checks of the site's validation.

It uses Page Objects, explicit waits (no `Thread.sleep`) and made-up test data. When a test fails it saves a screenshot and the page HTML. GitHub Actions runs the unit tests and the live flow in headless Chrome on every push.

</td>
<td width="50%" valign="top">

**📍 Quick facts**

| | |
|---|---|
| 🌐 **Target** | demo.guru99.com/telecom (live) |
| 🧪 **Tests** | 5 end-to-end · 24 unit |
| ✅ **CI** | 29 / 29 · ≈ 35 s for both jobs |
| 🧱 **Pattern** | Page Object Model · ordered JUnit 5 flow |
| 🛡️ **Data** | fictitious only, enforced by a test |
| 🧾 **On failure** | screenshot + HTML + visible text in the log |

</td>
</tr>
</table>

---

## ✅ Latest Results (GitHub Actions)

| Test | What is asserted | Result |
|---|---|:--:|
| **TC-01** Add customer | the confirmation page issues a numeric **customer id** | ✅ |
| **TC-02** Add tariff plan | *"Congratulation …"* confirmation | ✅ |
| **TC-03** Assign plan to that customer | at least one plan is offered, the newest is chosen, and *"Congratulation …"* appears | ✅ |
| **TC-04** Negative: digits in the first name | inline message *"Numbers are not allowed"* | ✅ |
| **TC-05** Negative: unknown customer id | no plans are offered | ✅ |
| **Unit** (`TestDataTest`) | 20 random customers meet the form rules · tariff values · id parsing · no real personal data in `src/` | ✅ 24 / 24 |

```mermaid
pie showData title 29 automated tests
    "End-to-end flow (live site)" : 3
    "Negative checks (live site)" : 2
    "Unit - generated customers (x20)" : 20
    "Unit - tariff, id parsing, data guard" : 4
```

```mermaid
xychart-beta
    title "Seconds per CI job"
    x-axis ["Unit tests", "End-to-end (live)"]
    y-axis "Seconds" 0 --> 25
    bar [14, 20]
```

### Before → after this update

```mermaid
xychart-beta
    title "Assertions in the test code"
    x-axis ["Before", "Now"]
    y-axis "Assertions" 0 --> 30
    bar [0, 29]
```

| | Before | Now |
|---|---|---|
| Compiles | ❌ the file name did not match the class name; no `pom.xml` | ✅ Maven project |
| Can a test fail? | ❌ `main()` printed "Failed …" and carried on | ✅ JUnit assertions |
| Tariff really assigned? | ❌ the plan was never chosen | ✅ plan chosen, confirmation asserted |
| Waiting | `Thread.sleep(5000)` × 6 = 30 s fixed | explicit waits, whole flow ≈ 10 s |
| Test data | the author's real address and mobile number | fictitious (`example.com`, 555-01xx) |
| CI | none | unit + live flow on every push |

---

## 🏗️ Architecture

```mermaid
flowchart LR
    T[TelecomFlowTest<br/>JUnit 5, ordered] --> P1[AddCustomerPage]
    T --> P2[AddTariffPage]
    T --> P3[AssignTariffPage]
    P1 & P2 & P3 --> BP[BasePage<br/>explicit waits · click with ad-overlay fallback]
    T --> TD[TestData<br/>fictitious customers + tariff]
    T --> FD[FailureDiagnostics<br/>PNG + HTML + text]
    BP --> BR[Browser<br/>Chrome · headless in CI<br/>ad domains blocked]
    BR --> SITE[(demo.guru99.com/telecom)]
    U[TestDataTest<br/>unit] --> TD
```

### 🔄 The business flow

```mermaid
sequenceDiagram
    autonumber
    participant T as Test
    participant AC as Add Customer
    participant AT as Add Tariff Plans
    participant AS as Assign Tariff Plan
    T->>AC: background check "Done", name, email, address, phone → Submit
    AC-->>T: confirmation page with Customer ID
    T->>AT: rental, minutes, SMS pack, per-minute charges → Submit
    AT-->>T: "Congratulation ... Tariff Plan"
    T->>AS: Customer ID → Submit
    AS-->>T: list of plans (radio buttons)
    T->>AS: choose the newest plan → Add Tariff Plan to Customer
    AS-->>T: "Congratulation ..." (assigned)
```

### 🧾 Form rules the site enforces (and the tests respect)

```mermaid
flowchart TB
    F[Add Customer form] --> N{First / last name}
    N -->|digits| E1["⛔ Numbers are not allowed"]
    F --> A{Address}
    A -->|"comma, # ..."| E2["⛔ Special characters are not allowed"]
    F --> S{Submit}
    S -->|any field invalid or empty| E3["⛔ alert: please fill all fields"]
    S -->|all valid| OK["✅ Customer ID issued"]
```

> The comma rule was found by this framework's own diagnostics. The first CI run failed with *"please fill all fields"*. The page object then reported `message3 = Special characters are not allowed` for the address *"545 Test Street, Springfield"*, and the test data was fixed (a unit test now enforces it).

### 🔁 CI pipeline

```mermaid
flowchart LR
    P([push · PR · manual]) --> U[Unit tests<br/>mvn test -Punit<br/>24]
    U --> E[End-to-end<br/>mvn test -Pe2e -Dheadless=true<br/>5 on the live site]
    E --> A[Artifacts:<br/>surefire reports,<br/>failure PNG + HTML]
```

---

## ▶️ Running the Tests

**You need:** JDK 17+, Maven 3.8+ and Google Chrome. Selenium Manager downloads the matching driver.

```bash
mvn test -Punit                     # unit tests, no browser
mvn test -Pe2e                      # live flow in a visible Chrome window
mvn test -Pe2e -Dheadless=true      # same, headless (what CI runs)
```

If a test fails, look in `target/failures/<test>.png` and `<test>.html`. The URL, title and visible text are also printed in the console.

---

## 📁 Project Structure

```text
Guru99TelTest/
├── .github/workflows/ci.yml
├── src/test/java/com/rbctcs/guru99/
│   ├── pages/      BasePage · AddCustomerPage · AddTariffPage · AssignTariffPage
│   ├── support/    Browser · TestData · FailureDiagnostics
│   ├── e2e/        TelecomFlowTest        (5 ordered tests on the live site)
│   └── unit/       TestDataTest           (24 tests, no browser)
├── pom.xml         Selenium 4.27 · JUnit 5.11 · profiles `unit` and `e2e`
└── LICENSE         MPL 2.0
```

```mermaid
pie showData title 500 lines of test code by layer
    "Page Objects" : 226
    "Support (browser, data, diagnostics)" : 118
    "End-to-end tests" : 91
    "Unit tests" : 65
```

---

## 🧹 Maintenance Log

The README was checked against the code, and these issues were fixed:

| Issue found | Fix |
|---|---|
| ❌ **The code did not compile.** `Guru99TelTestCase.java` declared the public class `Guru99TelTestClass` in `package com.example` at the repo root, and there was no `pom.xml`. | Rebuilt as a Maven project with packages and Page Objects |
| 🙈 **Nothing could fail.** `main()` printed "Failed to …" and carried on, and errors were caught and printed. | JUnit 5 tests with assertions; failures save a screenshot, the HTML and the page text |
| 📋 **The tariff was never assigned.** After entering the customer id, the old code never chose a plan or confirmed. | TC-03 chooses the newest plan, confirms it, and asserts the *Congratulation* page |
| 🏠 **Real personal data.** The test typed a real home address and mobile number into a public demo site, and published them in the source. | Fictitious data only (`example.com`, 555-01xx phone numbers), with a unit test that fails if a real-looking number or that address returns |
| ⏱️ **Fixed waits.** Six `Thread.sleep(5000)` calls took 30 s, and the static driver was shared. | Explicit waits. The whole live flow takes about 10 s. |
| 📣 **Ad overlays.** The demo site's Google "vignette" ads can steal clicks. | Ad domains are blocked in Chrome, and clicks fall back to JavaScript when intercepted |
| 📝 **The README was a single word** ("RbcTcs") | This README |

---

## 📈 Roadmap

- [ ] Pay Billing: check the usage and charges of the assigned plan
- [ ] Data-driven negative cases (email format, phone length, tariff limits) with `@ParameterizedTest`
- [ ] Add-customer variants: *Pending* background check, Reset button
- [ ] Allure report as a CI artifact
- [ ] Nightly scheduled run to catch changes on the demo site

---

## 🔗 Related Projects

| Project | Stack |
|---|---|
| 🛒 [Amazon · Google · Gmail BDD](https://github.com/rbchy/AmazonSeleniumTest) | Selenium · Cucumber · bot-wall aware CI |
| 🏔️ [NorthFace × Automation Exercise BDD](https://github.com/rbchy/NorthFace_AutomationExercise-BDD-Test-Automation) | Selenium · Cucumber · RestAssured · CI |
| 🏦 [ParaBank Banking Automation](https://github.com/rbchy/ParaBank-BankingAutomationJavaSelenium) | Java · Selenium · CI |
| 📱 [WDIO Native Demo App – Appium](https://github.com/rbchy/WebdriverIO-Native-DemoApp-MobileTestAutomationJavaAppiumTestNG) | Appium 2 · TestNG · Android emulator in CI |

<sub>All projects: [github.com/rbchy](https://github.com/rbchy)</sub>

---

## ⚠️ Disclaimer

This is a portfolio / training project against a public practice site owned by Guru99. Every run creates a few fictitious records there. Never use real personal data on demo sites.

---

## 👤 Author

<div align="center">

**Ranajit Baran Chowdhury (RB Chowdhury)**
QA Automation Engineer / SDET · West Norriton, Pennsylvania, USA

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/rbchy/)
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/rbchy)
[![Website](https://img.shields.io/badge/Website-rbchy.github.io%2Frbchy-0d9488?style=for-the-badge&logo=githubpages&logoColor=white)](https://rbchy.github.io/rbchy/)
[![Portfolio](https://img.shields.io/badge/Portfolio-Website-blueviolet?style=for-the-badge&logo=wix&logoColor=white)](https://rbc6543.wixsite.com/rbc-portfolio)
[![Gmail](https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:chyranajit@gmail.com)

<sub>Licensed under the Mozilla Public License 2.0 — see [LICENSE](LICENSE).</sub>

⭐ *If this project helps you, a star on the repo is appreciated.*

</div>
