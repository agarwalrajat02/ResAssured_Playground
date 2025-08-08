# Rest Assured API Automation Framework

## Overview
This project is a **Rest Assured-based API automation framework** built using **Java**, **TestNG**, **Maven**, and **Allure Reports**.  
It covers CRUD API test cases, token-based authentication, negative and positive test scenarios, and follows a clean, reusable, and maintainable architecture.

---

## Testing Strategy

### 1. Approach for Writing Test Flows
- **Requirement Analysis**: Understood the API endpoints, request/response structures, authentication mechanisms, and expected business flows.
- **Positive & Negative Coverage**:
  - **Positive**: Verified happy-path scenarios (valid payloads, correct authentication, valid inputs).
  - **Negative**: Covered invalid payloads, missing parameters, invalid authentication tokens, and edge cases.
- **Reusability**:
  - All common HTTP methods (GET, POST, PUT, DELETE, PATCH) are placed in a dedicated `ApiUtils` utility class.
  - Token retrieval is centralized in `TokenManager.getToken()` to avoid duplication.
  - Random data generation is handled by `RandomDataGenerator` for test independence.
- **Test Flow Structure**:
  - Grouped logically using TestNG `@Groups` (e.g., `sanity`, `regression`, `positive`, `negative`).
  - Prioritization handled via TestNG `priority` to execute critical tests first.
  - Separate test classes per API endpoint for clarity.

---

### 2. Ensuring Reliability & Maintainability
- **Base Class for Setup**:
  - Centralized `BaseClass` for setting up Rest Assured configurations and environment handling.
- **Environment Management**:
  - Configurable environments (`dev`, `qa`, `stage`, `prod`) handled via `config.properties` and `ConfigManager` utility.
- **Assertions**:
  - Strong assertions for HTTP status codes, response bodies, and schema validations where applicable.
- **Data Independence**:
  - Dynamic data creation and cleanup (e.g., creating a book and deleting it after test completion).
- **Clear Naming & Structure**:
  - Consistent naming conventions for test methods and variables to improve readability.

---

### 3. Integration with GitHub Actions
The framework is integrated with **GitHub Actions** for CI/CD:
- **Triggers**:
  - On push to `main` branch
  - On pull requests
  - Manual trigger via `workflow_dispatch` with environment selection.
- **Steps**:
  1. Checkout code
  2. Set up Java
  3. Install Maven dependencies
  4. Run TestNG suite
  5. Generate Allure report
  6. Upload Allure report as a GitHub Actions artifact
- **Branch Strategy**:
  - `main` branch holds stable production-ready automation code.
  - Feature branches for new API endpoints or enhancements.

---

### 4. Publishing Allure Reports via GitHub Pages
- GitHub Actions workflow publishes the **Allure HTML report** to the `gh-pages` branch after each run.
- GitHub Pages is enabled on the repository to serve the report link publicly.
- Example:

https://github.com/agarwalrajat02/ResAssured_Playground

- This allows anyone (with repository access) to view the latest test execution report without needing to run the tests locally.

---

### 5. Challenges & Solutions
| Challenge | Solution |
|-----------|----------|
| Handling dynamic authentication tokens | Centralized token retrieval in `TokenManager` with caching. |
| Data conflicts due to static payloads | Used `RandomDataGenerator` to create unique test data for each run. |
| Managing multiple environments | Created `config.properties` and `ConfigManager` to dynamically switch URLs and credentials. |
| Large number of common methods scattered in tests | Created `ApiUtils` to centralize all HTTP method calls. |
| Sharing reports without local setup | Used GitHub Actions + GitHub Pages to publish Allure reports online. |

---

## Tech Stack
- **Java 17**
- **Maven**
- **TestNG**
- **Rest Assured**
- **Allure Reports**
- **GitHub Actions**

---

## How to Run Tests Locally
```bash
mvn clean test

Directory Folder Structure 

src
 ├── main
 │   └── java
 │       ├── com.project.base         # Base setup
 │       ├── com.rest_assured.common  # Utilities (ApiUtils, TokenManager, RandomDataGenerator)
 │       ├── com.rest_assured.constants # Constants
 │       └── ...
 ├── test
 │   └── java
 │       └── com.package.rest_assured_framework # Test classes
config.properties
README.md
pom.xml

CI/CD WorkFlow File Location 
.github/workflows/api-tests.yml
