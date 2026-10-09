# 🚀 TestForge Automation Framework

[![TestForge Automation](https://github.com/Hunny-boxer/testforge-automation/actions/workflows/maven.yml/badge.svg)](https://github.com/Hunny-boxer/testforge-automation/actions/workflows/maven.yml)
![Java](https://img.shields.io/badge/Java-17-orange)
![Maven](https://img.shields.io/badge/Maven-Build-blue)
![Selenium](https://img.shields.io/badge/UI%20Automation-Selenium-green)
![REST Assured](https://img.shields.io/badge/API%20Testing-REST%20Assured-brightgreen)
![TestNG](https://img.shields.io/badge/Test%20Framework-TestNG-red)
![License](https://img.shields.io/badge/License-MIT-yellow)

**TestForge** is a Java-based UI and API test automation framework designed to support maintainable automated testing, reusable components, test suite separation, and continuous integration.

## ✨ Key Features

- **UI Automation:** Selenium WebDriver with reusable page objects.
- **API Testing:** REST Assured for HTTP requests and response validation.
- **Test Management:** TestNG annotations, groups, and suite execution.
- **Reporting:** ExtentReports with test names, categories, and execution results.
- **CI/CD:** GitHub Actions workflows for automated test execution.
- **Suite Separation:** Full, smoke, regression, API smoke, and API regression suites.
- **Build Management:** Maven dependency and test execution management.

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Maven | Build and dependency management |
| Selenium WebDriver | Browser UI automation |
| TestNG | Test execution and grouping |
| REST Assured | API automation |
| ExtentReports | Test execution reporting |
| GitHub Actions | Continuous integration |

## 🧪 Test Suites

| Suite | Command |
|---|---|
| Full suite | `mvn clean test` |
| Smoke suite | `mvn clean test -Psmoke` |
| Regression suite | `mvn clean test -Pregression` |
| API smoke suite | `mvn clean test -Papi-smoke` |
| API regression suite | `mvn clean test -Papi-regression` |

## ⚙️ Continuous Integration

GitHub Actions executes the test suites on pushes to `main` and pull requests targeting `main`. The workflow also uploads ExtentReports as build artifacts when the report file is generated.

**[View workflow runs and test results](https://github.com/Hunny-boxer/testforge-automation/actions)**

## 📊 Test Reports

After a workflow run:

1. Open the relevant run in GitHub Actions.
2. Scroll to the **Artifacts** section.
3. Download the report artifact for the suite you want to inspect.
4. Open `ExtentReport.html` in a browser.

## 📁 Repository

**[Explore the source code](https://github.com/Hunny-boxer/testforge-automation)**

This project demonstrates UI and API automation concepts, reusable test framework design, suite-based execution, reporting, and CI integration.

## 👨‍💻 Author

**Hunny Boxer**

GitHub: [@Hunny-boxer](https://github.com/Hunny-boxer)

---

*Built to demonstrate practical test automation engineering and continuous integration workflows.*
