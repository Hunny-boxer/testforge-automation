# Changelog

All notable changes to TestForge Automation are documented here.

## [1.0.0] - 2026-10-14

### Added
- Java 17-based UI and API test automation framework.
- Selenium WebDriver UI automation using the Page Object Model.
- REST Assured API testing with positive, negative, schema, and security assertions.
- Dedicated UI and API smoke, regression, and performance test suites.
- Centralized configuration with system-property, environment-variable, and properties-file fallback support.
- Thread-local WebDriver references for browser-session isolation between threads.
- Safer browser initialization, replacement, cleanup, and failure handling.
- ExtentReports HTML reporting and JUnit test-result publishing.
- Automated GitHub Actions CI workflows for six test suites.
- JaCoCo code coverage reporting and quality gates.

### Security
- Sanitized API diagnostic logging to reduce the risk of sensitive information exposure.
- CodeQL security analysis.
- Dependabot configuration for dependency updates.
- Environment-variable and GitHub Actions secret-based API token configuration.
- Protected `main` branch with pull requests and required CI checks.

### Testing and quality
- Deterministic configuration-resolution unit tests.
- Browser lifecycle and thread-isolation tests using lightweight fake WebDriver instances.
- API negative testing, JSON Schema validation, and security-header assertions.
- Automated coverage enforcement requiring at least 90% instruction coverage and 75% branch coverage.

### Release validation
- All six GitHub Actions test-suite jobs passed for the v1.0.0 release commit.
- Both CodeQL security-analysis jobs passed before publication.
- JaCoCo coverage checks passed as part of release validation.

### Release status
- TestForge v1.0.0 was published as the stable release on 2026-10-14.
- [View the v1.0.0 GitHub release](https://github.com/Hunny-boxer/testforge-automation/releases/tag/v1.0.0).
