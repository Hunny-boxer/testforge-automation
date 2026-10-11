# 🚀 TestForge — UI & API Automation Framework

[![TestForge CI](https://github.com/Hunny-boxer/testforge-automation/actions/workflows/maven.yml/badge.svg)](https://github.com/Hunny-boxer/testforge-automation/actions/workflows/maven.yml)
![Java 17](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)
![Selenium](https://img.shields.io/badge/UI%20Automation-Selenium-43B02A?logo=selenium)
![REST Assured](https://img.shields.io/badge/API%20Testing-REST%20Assured-brightgreen)
![TestNG](https://img.shields.io/badge/Test%20Framework-TestNG-red)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

**TestForge** is a Java-based test automation framework for validating web applications and REST APIs. It combines Selenium WebDriver, REST Assured, TestNG, Maven, ExtentReports, and GitHub Actions to demonstrate maintainable test automation and continuous integration.

The project separates UI and API test suites, provides HTML execution reports, and runs automated checks through independent GitHub Actions jobs.

**Repository:** [Hunny-boxer/testforge-automation](https://github.com/Hunny-boxer/testforge-automation)  
**CI pipeline:** [View workflow runs](https://github.com/Hunny-boxer/testforge-automation/actions)  
**Architecture:** [Explore the architecture diagram and execution flow](docs/ARCHITECTURE.md)

---

## ✨ Key Features

- **UI automation:** Selenium WebDriver with a reusable Page Object Model.
- **REST API testing:** REST Assured for HTTP requests, status-code validation, and response checks.
- **Test organization:** TestNG annotations, groups, and separate smoke and regression suites.
- **Performance checks:** API response-time measurements with a configurable threshold.
- **HTML reporting:** ExtentReports with test names, categories, results, and API execution details.
- **Continuous integration:** GitHub Actions runs test jobs on pushes to `main` and pull requests targeting `main`.
- **Execution artifacts:** HTML reports are uploaded from CI jobs for later review.
- **Execution summary:** The full-suite workflow publishes test totals and attempts to include API response-time metrics.

## 🏗️ Architecture & Execution Flow

Explore the architecture documentation to understand how the UI and API test layers, browser driver, reporting components, and CI pipeline work together.

**[📐 View TestForge Architecture & Execution Flow →](docs/ARCHITECTURE.md)**

The documentation includes a Mermaid architecture diagram, component responsibilities, execution steps, suite descriptions, and troubleshooting guidance.

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Maven | Build and dependency management |
| Selenium WebDriver | Browser automation |
| TestNG | Test execution, grouping, and suite configuration |
| REST Assured | REST API automation |
| ExtentReports | HTML test reporting |
| GitHub Actions | Continuous integration and test execution |

## 🧪 Test Suites

The pipeline is split into six jobs so that UI, API, and full-suite results can be reviewed separately.

| Suite | Execution command |
|---|---|
| Full test suite | `mvn -B clean test` |
| UI smoke | `mvn -B clean test -Dtest=GoogleSearchTest` |
| UI regression | `mvn -B clean test -Dtest=GoogleSearchRegressionTest` |
| API smoke | `mvn -B clean test -Papi-smoke` |
| API regression | `mvn -B clean test -Papi-regression` |
| API performance | `mvn -B clean test -Papi-performance` |

The individual UI commands target their respective test classes. The API commands use the Maven profiles configured in `pom.xml`.

Test counts can differ between runs as the framework evolves. Results from separate suites may overlap, so their totals should not be added together as a unique test count.

## 🌐 API Performance Validation

The API performance tests measure the response time of representative GET requests, including:

- `GET /users/{id}`
- `GET /posts/{id}`

Each response-time test checks the HTTP status code and verifies that the request completes within the configured threshold.

The API response-time threshold is currently configured in `ApiResponseTimeTest.java` as `10,000 ms`. This is a pass/fail limit for the test, not a claim that the API should normally take that long.

The framework also records endpoint, status-code, and response-time information for reporting and CI diagnostics.

## 📊 ExtentReports

TestForge generates HTML reports to help review test execution beyond the final build status.

Reports are uploaded as GitHub Actions artifacts when generated:

| Artifact | Contents |
|---|---|
| `extent-report-full` | Full-suite execution report |
| `extent-report-smoke` | UI smoke report |
| `extent-report-regression` | UI regression report |
| `extent-report-api-smoke` | API smoke report |
| `extent-report-api-regression` | API regression report |
| `extent-report-api-performance` | API performance report |

### How to view a report

1. Open [GitHub Actions](https://github.com/Hunny-boxer/testforge-automation/actions).
2. Select a workflow run.
3. Find the **Artifacts** section near the bottom of the run page.
4. Download the relevant report artifact.
5. Extract the downloaded ZIP and open `ExtentReport.html` in a browser.

## ⚙️ Continuous Integration

The GitHub Actions workflow is defined in `.github/workflows/maven.yml`.

The workflow provides six independent jobs:

1. Full Test Suite
2. Smoke Test Suite
3. Regression Test Suite
4. API Smoke Test Suite
5. API Regression Test Suite
6. API Performance Test Suite

The pipeline checks out the repository, configures Java 17, prepares Chrome for UI testing where required, executes the appropriate Maven command, and uploads generated report artifacts.

**[View all TestForge workflow runs →](https://github.com/Hunny-boxer/testforge-automation/actions)**

## 🗂️ Project Structure

```text
testforge-automation/
├── .github/
│   └── workflows/
│       └── maven.yml
├── docs/
│   └── ARCHITECTURE.md
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

### Design overview

- **Configuration and driver management:** Centralized browser and configuration utilities.
- **Page Object Model:** UI page interactions are separated from test methods.
- **Base test classes:** Shared setup and teardown for the framework.
- **API test layer:** Reusable request configuration, endpoint definitions, and API tests.
- **Reporting layer:** Listener-based capture of test outcomes and ExtentReports lifecycle management.
- **CI layer:** GitHub Actions executes the suites and publishes artifacts.

## ▶️ Running the Project

### Prerequisites for local execution

- JDK 17
- Maven
- Chrome for the UI tests
- Internet access for the configured demo endpoints and dependency downloads

Run the following commands from the repository root.

**Full suite**

```bash
mvn -B clean test
```

**UI smoke test**

```bash
mvn -B clean test -Dtest=GoogleSearchTest
```

**UI regression test**

```bash
mvn -B clean test -Dtest=GoogleSearchRegressionTest
```

**API smoke tests**

```bash
mvn -B clean test -Papi-smoke
```

**API regression tests**

```bash
mvn -B clean test -Papi-regression
```

**API performance tests**

```bash
mvn -B clean test -Papi-performance
```

If local software installation is restricted, GitHub Actions can run the configured jobs in GitHub's hosted environment.

## 🗺️ Roadmap

Potential next improvements include:

- Environment-specific configuration for QA, staging, and production.
- Richer API assertions and JSON schema validation coverage.
- Historical response-time trends and performance thresholds by endpoint.
- Improved failure diagnostics and browser screenshots.
- Pull-request quality gates and coverage reporting.

These are future enhancements, not claims that every item is already implemented.

## 👨‍💻 Author

**Hunny Boxer**

GitHub: [@Hunny-boxer](https://github.com/Hunny-boxer)

---

*TestForge is a learning and portfolio project demonstrating UI and API test automation, framework organization, reporting, and continuous integration.*
