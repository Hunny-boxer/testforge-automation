
# Changelog

All notable changes to TestForge Automation are documented here.

## [1.0.0] - Unreleased

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
- GitHub Actions runs the full suite along with UI smoke, UI regression, API smoke, API regression, and API performance suites.
- Release readiness requires all six test-suite jobs to pass.
- JaCoCo coverage checks and CodeQL analysis must pass before the stable release is published.

### Release status
- This section describes the planned stable v1.0.0 release.
- The release will be considered published only after the `v1.0.0` tag and GitHub release are created.
