

# 🚀 TestForge — UI & API Automation Framework

[![TestForge CI](https://github.com/Hunny-boxer/testforge-automation/actions/workflows/maven.yml/badge.svg)](https://github.com/Hunny-boxer/testforge-automation/actions/workflows/maven.yml)
![Java 17](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)
![Selenium](https://img.shields.io/badge/UI%20Automation-Selenium-43B02A?logo=selenium)
![REST Assured](https://img.shields.io/badge/API%20Testing-REST%20Assured-brightgreen)
![TestNG](https://img.shields.io/badge/Test%20Framework-TestNG-red)
![JaCoCo](https://img.shields.io/badge/Coverage-JaCoCo-orange)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

**TestForge** is a Java-based UI and API test automation framework designed to demonstrate reusable test architecture, automated validation, test reporting, code coverage, and continuous integration.

The framework combines Selenium WebDriver, REST Assured, TestNG, Maven, ExtentReports, JaCoCo, and GitHub Actions.

**Repository:** [Hunny-boxer/testforge-automation](https://github.com/Hunny-boxer/testforge-automation)  
**CI pipeline:** [GitHub Actions](https://github.com/Hunny-boxer/testforge-automation/actions)  
**Architecture:** [Architecture documentation](docs/ARCHITECTURE.md)

---

## ✨ Key Features

- **UI automation:** Selenium WebDriver with Page Object Model architecture.
- **API automation:** REST Assured for HTTP requests and response validation.
- **Test organization:** TestNG groups and separate smoke, regression, and performance suites.
- **Unit testing:** Dedicated tests for configuration management and driver-factory behavior.
- **Schema validation:** JSON Schema validation for API responses.
- **Negative testing:** Validation of unsuccessful and unexpected API scenarios.
- **Performance checks:** API response-time assertions against a defined threshold.
- **HTML reporting:** ExtentReports for reviewing test execution results.
- **JUnit results:** Test results published in GitHub Actions.
- **Code coverage:** JaCoCo HTML coverage report and raw execution data artifacts.
- **Continuous integration:** Six independent GitHub Actions jobs for UI and API testing.
- **Cloud-based execution:** Run the configured CI pipeline without installing Java, Maven, or Chrome on your work computer.

## 📸 TestForge in Action

### GitHub Actions — CI Pipeline

The workflow executes six independent jobs covering the full suite, UI smoke, UI regression, API smoke, API regression, and API performance.

![TestForge GitHub Actions CI run](docs/testforge-ci-run.png)

[View the latest workflow runs](https://github.com/Hunny-boxer/testforge-automation/actions)

### ExtentReports — HTML Test Report

TestForge generates HTML reports to make test outcomes easier to review.

![TestForge ExtentReports sample](docs/extent-report-sample.png)

To view a report, open a completed [GitHub Actions run](https://github.com/Hunny-boxer/testforge-automation/actions), find the **Artifacts** section, download the relevant report ZIP, extract it, and open `ExtentReport.html` in a browser.

## 🏗️ Architecture & Execution Flow

TestForge separates UI automation, API automation, shared setup, configuration, and reporting components.

**[Explore the architecture documentation →](docs/ARCHITECTURE.md)**

The framework uses:

- Centralized configuration through `ConfigManager`.
- Browser lifecycle management through `DriverFactory`.
- Page Object Model classes for UI interactions.
- Shared base test classes for setup and teardown.
- REST Assured for API requests and assertions.
- TestNG for test execution, grouping, and suite configuration.
- ExtentReports for HTML reporting.
- GitHub Actions for automated CI execution.

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Maven | Build and dependency management |
| Selenium WebDriver | Browser automation |
| TestNG | Test execution, grouping, and assertions |
| REST Assured | REST API testing |
| JSON Schema Validator | API response schema validation |
| ExtentReports | HTML test reporting |
| JaCoCo | Java code coverage measurement |
| GitHub Actions | Continuous integration |
| GitHub-hosted runners | Cloud-based test execution |

## 🧪 Automated Test Suites

The CI workflow contains six independent jobs.

| Suite | Maven command |
|---|---|
| Full test suite | `mvn -B clean verify` |
| UI smoke | `mvn -B clean test -Dtest=GoogleSearchTest` |
| UI regression | `mvn -B clean test -Dtest=GoogleSearchRegressionTest` |
| API smoke | `mvn -B clean test -Papi-smoke` |
| API regression | `mvn -B clean test -Papi-regression` |
| API performance | `mvn -B clean test -Papi-performance` |

The UI commands target their respective test classes. The API commands use Maven profiles configured in `pom.xml`.

The full-suite job runs Maven verification so the JaCoCo report can be generated.

Test counts can change as the framework evolves. Results from separate suites may overlap, so their totals should not be added together as a unique test count.

## 🧩 Unit Testing

TestForge includes focused unit tests for configuration and browser-driver management.

### Configuration tests

`ConfigManagerTest` checks:

- Loading the configured base URL.
- Loading the configured browser.
- Returning `null` for an unknown configuration key.

### Driver-factory tests

`DriverFactoryTest` checks:

- Rejecting unsupported browser names.
- Returning `null` when a driver has not been initialized.
- Safe cleanup when no driver exists.

The invalid-browser test does not need to launch Chrome. Browser initialization tests that require a real browser run in the hosted CI environment.

These tests provide baseline validation of framework utilities. They can be expanded as configuration and driver management become more sophisticated.

## 🌐 API Testing & Validation

The API tests exercise representative JSONPlaceholder endpoints, including:

- `GET /users/{id}` — retrieve a user.
- `GET /posts/{id}` — retrieve a post.
- `POST /users` — exercise simulated user creation.
- `PUT /users/{id}` — exercise simulated user updates.

The framework includes:

- HTTP status-code assertions.
- Response field validation.
- JSON Schema validation.
- Negative test cases.
- Shared API response specifications.
- Test data stored in resource files.

**Important:** JSONPlaceholder is a demo API. Its write operations simulate changes rather than persist resources like a production database. Assertions should reflect the behavior of this mock service.

## ⚡ API Performance Validation

The API performance tests measure response times for representative GET requests:

- `GET /users/{id}`
- `GET /posts/{id}`

The current response-time threshold is **10,000 ms**, configured in `ApiResponseTimeTest.java`.

This is a pass/fail test threshold, not a claim about typical API latency. Actual timings vary with network conditions and the demo service.

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

Artifacts are retained according to the workflow's configured retention period.

## 📈 JaCoCo Code Coverage

The full-suite workflow generates JaCoCo coverage artifacts during Maven verification.

| Artifact | Contents |
|---|---|
| `jacoco-coverage-report` | HTML coverage report |
| `jacoco-coverage-data` | Raw JaCoCo execution data |

### How to inspect coverage

1. Open [GitHub Actions](https://github.com/Hunny-boxer/testforge-automation/actions).
2. Select a successful full-suite workflow run.
3. Find `jacoco-coverage-report` under **Artifacts** and download it.
4. Extract the ZIP archive.
5. Open `index.html` in the extracted report directory.
6. Navigate through packages and classes to inspect instruction, branch, and missed-code metrics.

JaCoCo measures the code included in its report. Coverage percentages should therefore be interpreted in the context of the classes measured, not as a percentage of every possible test scenario or the entire repository.

Coverage is a diagnostic metric; high coverage alone does not guarantee meaningful assertions or reliable tests.

## ⚙️ Continuous Integration

The workflow is defined in `.github/workflows/maven.yml`.

On configured pushes to `main` and pull requests targeting `main`, GitHub Actions runs the relevant workflow jobs.

The pipeline:

1. Checks out the repository.
2. Sets up Java 17.
3. Prepares Chrome for UI tests where required.
4. Executes the relevant Maven test command.
5. Publishes JUnit test results.
6. Publishes the full-suite execution summary.
7. Uploads ExtentReports artifacts.
8. Uploads JaCoCo coverage artifacts for the full-suite job.

**[View all workflow runs →](https://github.com/Hunny-boxer/testforge-automation/actions)**

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
│   │   ├── java/com/hunny/testforge/
│   │   │   ├── config/
│   │   │   │   └── ConfigManager.java
│   │   │   ├── driver/
│   │   │   │   └── DriverFactory.java
│   │   │   └── pages/
│   │   │       └── GooglePage.java
│   │   └── resources/
│   │       └── config.properties
│   └── test/
│       ├── java/com/hunny/testforge/
│       │   ├── api/
│       │   ├── base/
│       │   ├── config/
│       │   │   └── ConfigManagerTest.java
│       │   ├── driver/
│       │   │   └── DriverFactoryTest.java
│       │   ├── reporting/
│       │   ├── tests/
│       │   └── utils/
│       └── resources/
│           ├── schemas/
│           ├── testdata/
│           └── testng*.xml
├── pom.xml
├── LICENSE
└── README.md
```

## ▶️ Running the Project

### Prerequisites for local execution

- JDK 17
- Maven
- Chrome for UI tests
- Internet access for dependencies and configured demo endpoints

Run commands from the repository root.

**Full test suite**

```bash
mvn -B clean verify
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
- Pull-request quality gates and code coverage thresholds.
- More focused unit tests for framework utilities.

These are planned enhancements, not claims that every item is already implemented.

## 👨‍💻 Author

**Hunny Boxer**

GitHub: [@Hunny-boxer](https://github.com/Hunny-boxer)

---

*TestForge is a portfolio project demonstrating UI and API automation, reusable framework architecture, unit testing, reporting, code coverage, and continuous integration.*

