
# 🚀 TestForge — UI & API Automation Framework

[![TestForge CI](https://github.com/Hunny-boxer/testforge-automation/actions/workflows/maven.yml/badge.svg)](https://github.com/Hunny-boxer/testforge-automation/actions/workflows/maven.yml)
![Java 17](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)
![Selenium](https://img.shields.io/badge/UI%20Automation-Selenium-43B02A?logo=selenium)
![REST Assured](https://img.shields.io/badge/API%20Testing-REST%20Assured-brightgreen)
![TestNG](https://img.shields.io/badge/Test%20Framework-TestNG-red)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

**TestForge** is a Java-based UI and API test automation framework built to demonstrate reusable test architecture, automated validation, HTML reporting, and continuous integration.

It combines Selenium WebDriver, REST Assured, TestNG, Maven, ExtentReports, and GitHub Actions in one portfolio project.

**Repository:** [Hunny-boxer/testforge-automation](https://github.com/Hunny-boxer/testforge-automation)  
**CI pipeline:** [View GitHub Actions](https://github.com/Hunny-boxer/testforge-automation/actions)  
**Architecture:** [Architecture and execution flow](docs/ARCHITECTURE.md)

---

## ✨ Key Features

- **UI automation:** Selenium WebDriver with Page Object Model architecture.
- **API automation:** REST Assured for HTTP requests and response validation.
- **Test organization:** TestNG groups and separate smoke, regression, and performance suites.
- **Schema validation:** JSON Schema validation for API responses.
- **Performance checks:** API response-time measurements against a configured threshold.
- **HTML reporting:** ExtentReports for reviewing test execution results.
- **Continuous integration:** GitHub Actions executes the configured suites.
- **Report artifacts:** HTML reports are uploaded for download after workflow runs.
- **Execution summary:** The full-suite workflow generates a GitHub Actions summary from available test logs and reports.

## 📸 TestForge in Action

### GitHub Actions — CI Pipeline

The repository uses six independent workflow jobs to execute UI and API tests.

![TestForge GitHub Actions CI run](docs/testforge-ci-run.png)

[View the successful CI run](https://github.com/Hunny-boxer/testforge-automation/actions/runs/38131224018)

### ExtentReports — HTML Test Report

TestForge generates HTML reports to make test outcomes easier to review.

![TestForge ExtentReports sample](docs/extent-report-sample.png)

To view the latest report, open a [GitHub Actions workflow run](https://github.com/Hunny-boxer/testforge-automation/actions), find its Artifacts section, download the relevant report ZIP, extract it, and open `ExtentReport.html` in a browser.

## 🏗️ Architecture & Execution Flow

TestForge separates UI automation, API automation, shared setup, and reporting components.

**[📐 Explore the architecture documentation →](docs/ARCHITECTURE.md)**

The architecture documentation describes the main components, execution flow, suite organization, and troubleshooting guidance.

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Maven | Build and dependency management |
| Selenium WebDriver | Browser automation |
| TestNG | Test execution, grouping, and suite configuration |
| REST Assured | REST API automation |
| JSON Schema Validator | API response schema validation |
| ExtentReports | HTML test reporting |
| GitHub Actions | Continuous integration |

## 🧪 Automated Test Suites

The workflow contains six independent jobs.

| Suite | Maven command |
|---|---|
| Full test suite | `mvn -B clean test` |
| UI smoke | `mvn -B clean test -Dtest=GoogleSearchTest` |
| UI regression | `mvn -B clean test -Dtest=GoogleSearchRegressionTest` |
| API smoke | `mvn -B clean test -Papi-smoke` |
| API regression | `mvn -B clean test -Papi-regression` |
| API performance | `mvn -B clean test -Papi-performance` |

The UI commands target their respective test classes. The API commands use Maven profiles configured in `pom.xml`.

Test counts can change as the framework evolves. Results from separate suites may overlap, so their totals should not be added together as a unique test count.

## 🌐 API Testing & Validation

The API tests exercise representative JSONPlaceholder endpoints, including:

- `GET /users/{id}` — retrieve a user.
- `GET /posts/{id}` — retrieve a post.
- `POST /users` — exercise simulated user creation.
- `PUT /users/{id}` — exercise simulated user updates.

The framework includes status-code assertions, response field validation, JSON Schema validation, and negative test cases.

**Note:** JSONPlaceholder is a demo API. Its write operations simulate changes rather than persist resources like a production database. Negative tests should reflect the behavior of this mock service.

## ⚡ API Performance Validation

The API performance tests measure response times for representative GET requests:

- `GET /users/{id}`
- `GET /posts/{id}`

The current response-time threshold is configured in `ApiResponseTimeTest.java` as `10,000 ms`.

This is a test pass/fail threshold, not a claim about normal API latency. Actual timings vary with network conditions and the demo service.

## 📊 ExtentReports

TestForge uploads HTML report artifacts when reports are generated.

| Artifact | Contents |
|---|---|
| `extent-report-full` | Full-suite execution report |
| `extent-report-smoke` | UI smoke report |
| `extent-report-regression` | UI regression report |
| `extent-report-api-smoke` | API smoke report |
| `extent-report-api-regression` | API regression report |
| `extent-report-api-performance` | API performance report |

### How to download a report

1. Open [GitHub Actions](https://github.com/Hunny-boxer/testforge-automation/actions).
2. Select a completed workflow run.
3. Scroll to the **Artifacts** section.
4. Download the report artifact you need.
5. Extract the ZIP archive.
6. Open `ExtentReport.html` in your browser.

## ⚙️ Continuous Integration

The workflow is defined in `.github/workflows/maven.yml`.

On pushes to `main` and pull requests targeting `main`, GitHub Actions runs the configured workflow jobs.

The pipeline:

1. Checks out the repository.
2. Sets up Java 17.
3. Prepares Chrome for UI tests where required.
4. Executes the relevant Maven test command.
5. Publishes the full-suite execution summary.
6. Uploads generated HTML report artifacts.

**[🚀 View all workflow runs →](https://github.com/Hunny-boxer/testforge-automation/actions)**

## 🗂️ Project Structure

```text
testforge-automation/
├── .github/
│   └── workflows/
│       └── maven.yml
├── docs/
│   ├── ARCHITECTURE.md
│   ├── extent-report-sample.png
│   └── testforge-ci-run.png
├── src/
│   ├── main/
│   │   └── java/com/hunny/testforge/
│   │       ├── config/
│   │       ├── driver/
│   │       └── pages/
│   └── test/
│       ├── java/com/hunny/testforge/
│       │   ├── api/
│       │   ├── base/
│       │   ├── reporting/
│       │   └── tests/
│       └── resources/
│           ├── schemas/
│           ├── testdata/
│           └── testng*.xml
├── pom.xml
├── LICENSE
└── README.md
```

### Design Overview

- **Configuration and driver management:** Centralized browser and configuration utilities.
- **Page Object Model:** Separates UI interactions from test methods.
- **Base test classes:** Provides shared setup and teardown.
- **API test layer:** Organizes request configuration, endpoint definitions, and API tests.
- **Reporting layer:** Manages test outcome capture and ExtentReports lifecycle.
- **CI layer:** Runs test suites and publishes artifacts through GitHub Actions.

## ▶️ Running the Project

### Prerequisites for local execution

- JDK 17
- Maven
- Chrome for UI tests
- Internet access for dependencies and configured demo endpoints

Run commands from the repository root.

**Full test suite**

```bash
mvn -B clean test
```

**UI smoke**

```bash
mvn -B clean test -Dtest=GoogleSearchTest
```

**UI regression**

```bash
mvn -B clean test -Dtest=GoogleSearchRegressionTest
```

**API smoke**

```bash
mvn -B clean test -Papi-smoke
```

**API regression**

```bash
mvn -B clean test -Papi-regression
```

**API performance**

```bash
mvn -B clean test -Papi-performance
```

If installing software locally is restricted, GitHub Actions can run the configured jobs in GitHub's hosted environment.

## 🗺️ Roadmap

Potential future improvements include:

- Environment-specific configuration for QA, staging, and production.
- Broader API schema validation and negative test coverage.
- Historical API response-time trends.
- Improved failure diagnostics and browser screenshots.
- Pull-request quality gates and code coverage reporting.

These are planned enhancements, not claims that every item is already implemented.

## 👨‍💻 Author

**Hunny Boxer**

GitHub: [@Hunny-boxer](https://github.com/Hunny-boxer)

---

*TestForge is a portfolio project demonstrating UI and API automation, reusable framework architecture, reporting, and continuous integration.*
