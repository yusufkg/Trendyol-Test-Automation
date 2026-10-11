# QA Automation Project

**Java 17 · Selenium WebDriver · Cucumber · Gherkin · JUnit · Maven ·
XPath**

A Java-based test automation learning project demonstrating **Page
Object Model (POM)**, **Behavior-Driven Development (BDD)**, browser
automation, unit testing, and HTML test reporting.

> **Project status: In progress.** Five unit tests pass. The Trendyol
> end-to-end scenario is implemented as a test flow but is not yet
> passing. SQL Server logging is planned/configurable but has not been
> verified.

## Overview

This repository contains two testing areas:

-   **E-commerce web automation:** A Cucumber scenario designed to open
    Trendyol, allow manual account login, search for `Samsung S20`, sort
    products by ascending price, add an eligible product to the cart,
    and verify the cart.
-   **Phonebook validation:** Java unit tests for contact validation,
    with SQL Server-backed CRUD operations as a development goal.

The project separates human-readable test scenarios from Java step
definitions and Selenium page interactions.

## Tech Stack

  -----------------------------------------------------------------------
  Technology                          Purpose
  ----------------------------------- -----------------------------------
  Java 17                             Core language

  Selenium WebDriver                  Browser automation

  ChromeDriver                        Chrome browser control

  Cucumber + Gherkin                  BDD feature scenarios

  JUnit                               Unit testing and assertions

  Maven                               Build, dependencies, and test
                                      execution

  XPath                               Web element locators

  Page Object Model                   Maintainable test architecture

  HTML reporting                      Test results and failure evidence

  SQL Server + JDBC                   Planned/unfinished CRUD and
                                      execution logging integration
  -----------------------------------------------------------------------

## Project Structure

The tree below includes **confirmed project files** and clearly marked
**suggested folders**. Suggested folders may not exist in the current
repository.

``` text
qa-automation-project/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   └── java/com/yusuf/qa/
│   │       ├── phonebook/                 # Phonebook application logic
│   │       ├── database/                  # Suggested: JDBC/SQL access
│   │       └── config/                    # Suggested: environment configuration
│   └── test/
│       ├── java/com/yusuf/qa/
│       │   ├── pages/
│       │   │   ├── TrendyolPage.java      # Selenium page actions and XPath
│       │   │   └── TrendyolPriceTest.java # Price-related unit tests
│       │   ├── steps/
│       │   │   └── TrendyolSteps.java     # Cucumber step definitions
│       │   ├── runners/
│       │   │   └── TrendyolIT.java        # Cucumber/JUnit runner
│       │   ├── phonebook/
│       │   │   └── ContactValidationTest.java
│       │   ├── hooks/                     # Suggested: test lifecycle hooks
│       │   └── utils/                     # Suggested: shared helpers
│       └── resources/
│           └── features/
│               ├── trendyol.feature
│               └── demo.feature
└── target/                                # Maven-generated output; do not commit
    ├── trendyolit-report.html
    └── surefire-reports/
```

### Responsibilities

  --------------------------------------------------------------------------------
  Layer                   Location                         Responsibility
  ----------------------- -------------------------------- -----------------------
  Feature                 `src/test/resources/features/`   Defines **what** is
                                                           tested

  Step Definitions        `src/test/java/.../steps/`       Maps Gherkin steps to
                                                           Java

  Page Objects            `src/test/java/.../pages/`       Defines **how** browser
                                                           interactions work

  Runner                  `src/test/java/.../runners/`     Starts Cucumber
                                                           scenarios

  Unit Tests              `src/test/java/.../`             Checks isolated Java
                                                           behavior

  Database                Suggested                        SQL access and
                          `src/main/java/.../database/`    persistence

  Reports                 `target/`                        Stores generated test
                                                           outputs
  --------------------------------------------------------------------------------

### Execution Flow

``` text
trendyol.feature
      |
      v
TrendyolIT.java
      |
      v
TrendyolSteps.java
      |
      v
TrendyolPage.java
      |
      v
Selenium WebDriver + Chrome
      |
      v
Assertions / Screenshots / HTML Report
      |
      v
SQL Server test logging (not configured yet)
```

## Prerequisites

-   **JDK 17**
-   **Apache Maven**
-   **Google Chrome**
-   **IntelliJ IDEA** or another Java IDE
-   **SQL Server**, only if working on database integration

Check Java and Maven:

``` bash
java -version
mvn -version
```

## Getting Started

1.  Clone the repository (replace the placeholder with the actual
    repository URL):

    ``` bash
    git clone https://github.com/<your-username>/<your-repository>.git
    cd <your-repository>
    ```

2.  Run the default Maven tests:

    ``` bash
    mvn clean test
    ```

3.  Run the Trendyol Cucumber test:

    **PowerShell (Windows):**

    ``` powershell
    mvn "-Dtest=TrendyolIT" test
    ```

    **Bash:**

    ``` bash
    mvn -Dtest=TrendyolIT test
    ```

4.  Open the generated report (if created):

    **PowerShell:**

    ``` powershell
    Start-Process ".\target\trendyolit-report.html"
    ```

    **Bash:**

    ``` bash
    # Open target/trendyolit-report.html in your browser.
    ```

> **Note:** The Trendyol browser scenario currently fails during the
> product-search step because the search field is not located as a
> clickable element. A passing Maven unit-test run does **not** mean the
> end-to-end scenario passes.

## Example BDD Scenario

The current project uses **Turkish Gherkin step definitions**. An
illustrative English version of the intended workflow is shown below; it
is **not a drop-in replacement** unless the Java step annotations are
updated to match.

``` gherkin
@trendyol
Feature: Trendyol product shopping

  Scenario: Add an eligible Samsung S20 product to the cart
    Given I open the Trendyol homepage
    When I complete the manual login
    And I click the search box
    And I type "Samsung S20" into the search box
    And I press Enter
    And I wait for the search results
    And I select sorting by lowest price
    And I add the cheapest eligible product to the cart
    Then I verify that "Samsung S20" is in the cart
```

### How the Layers Work Together

**Step definition (illustrative):**

``` java
@When("I type {string} into the search box")
public void typeProduct(String productName) {
    trendyolPage.search(productName);
}
```

**Page Object (illustrative):**

``` java
private final By searchBox = By.xpath("//input[@type='search']");

public void search(String productName) {
    WebElement input = wait.until(
        ExpectedConditions.elementToBeClickable(searchBox)
    );
    input.clear();
    input.sendKeys(productName);
    input.sendKeys(Keys.ENTER);
}
```

These snippets illustrate the architecture; they are **not independently
compilable classes**. The `trendyolPage` and `wait` fields must exist,
and the example XPath must be checked against the live page.

## Test Results

Based on the latest shared run from **October 11, 2026**:

  -----------------------------------------------------------------------
  Test / capability                   Status
  ----------------------------------- -----------------------------------
  `TrendyolPriceTest`                 **3 passed**

  `ContactValidationTest`             **2 passed**

  Unit tests total                    **5 passed, 0 failed**

  Chrome browser launch               Working

  Manual-login confirmation dialog    Working as user confirmation; login
                                      state is not independently verified

  `TrendyolIT` end-to-end scenario    **Failing**

  Product search                      `TimeoutException` waiting for
                                      clickable XPath

  Sorting and cart verification       Not reached / not verified

  HTML report                         Generated at
                                      `target/trendyolit-report.html`

  SQL Server logging                  Skipped because `QA_DB_URL` is not
                                      configured
  -----------------------------------------------------------------------

### Known Issues

**1. Product search locator**

``` text
org.openqa.selenium.TimeoutException:
waiting for element to be clickable:
By.xpath:
//input[@data-testid='suggestion' or contains(@placeholder,'Ara') or @type='search']
```

The locator and page state must be investigated using Chrome DevTools.
Cookie dialogs, overlays, and layout changes should also be checked.

**2. Browser DevTools warning**

The project has shown a CDP version warning with Chrome 154 and Selenium
4.27.0. This warning has **not** been established as the cause of the
XPath timeout.

**3. Database logging**

``` text
SQL logging skipped: QA_DB_URL is not configured
```

The database URL and credentials must be supplied securely before
verifying SQL logging. Do not commit passwords, tokens, or local
connection secrets.

## Roadmap

-   [x] Java 17 and Maven project setup
-   [x] Cucumber feature files and step definitions
-   [x] Selenium Page Object layer
-   [x] Five passing unit tests
-   [x] HTML report generation
-   [ ] Fix and verify the Trendyol search locator
-   [ ] Verify ascending-price sorting and add-to-cart
-   [ ] Validate actual login state after manual confirmation
-   [ ] Complete and verify phonebook SQL Server CRUD
-   [ ] Configure SQL Server execution logging (`QA_DB_URL`)
-   [ ] Add reliable failure handling and screenshots
-   [ ] Add `.gitignore` and secure configuration examples
-   [ ] Add Jenkins CI pipeline

## Suggested `.gitignore`

Create a `.gitignore` file in the repository root:

``` gitignore
target/
.idea/
*.iml
*.log
.env
.env.*
!.env.example
```

Do not add real database credentials to `.env.example`.

## Skills Demonstrated

-   Java test automation and basic OOP
-   Selenium WebDriver with XPath and explicit waits
-   Cucumber/Gherkin BDD test design
-   Page Object Model architecture
-   JUnit and Maven execution
-   Debugging Selenium errors and reviewing HTML reports
-   Planning SQL Server/JDBC integration

## Portfolio / CV Description

**QA Automation Framework --- Personal Project (In Progress)**

Developed a Java-based test automation project using Selenium WebDriver,
Cucumber, JUnit, and Maven with a Page Object Model architecture.
Structured BDD scenarios using Gherkin, Java step definitions, and
reusable page interactions. Executed five passing unit tests and
generated HTML test reports. End-to-end e-commerce automation and SQL
Server integration remain in development.

------------------------------------------------------------------------

**Disclaimer:** This is an independent educational/portfolio project and
is not affiliated with Trendyol. The test interacts with a live
third-party website whose interface and access controls may change.
