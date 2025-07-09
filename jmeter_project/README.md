# JMeter Project Boilerplate - Super Modular & Code-Centric

This project provides a highly modular, maintainable, and DRY (Don't Repeat Yourself) boilerplate for JMeter tests, designed for team collaboration and automation, with a strong emphasis on writing test logic as pure Groovy code.

## Key Principles

-   **Pure Code Test Logic:** All test steps and logic are written in external Groovy scripts, minimizing JMX XML interaction.
-   **Thin JMX Wrapper:** JMX files primarily serve as orchestrators, pointing to a single Groovy test runner script.
-   **External Configuration:** Manage all global settings and environment-specific variables outside of JMX files.
-   **DRY (Don't Repeat Yourself):** Centralize reusable functions and logic in shared Groovy scripts (Utils, FakerUtils).

## Structure

-   `config/`: Stores global and environment-specific configuration files.
    -   `settings.properties`: Example properties file for base URLs, protocols, etc.
-   `data/`: Stores test data (e.g., CSV, JSON).
    -   `csv/`: CSV files.
    -   `json/`: JSON files.
-   `docs/`: Project documentation.
-   `lib_ext/`: External JAR libraries (e.g., `java-faker.jar`). Place downloaded JARs here.
-   `reports/`: Test execution reports (e.g., HTML, JTL).
-   `scripts/`: Custom Groovy scripts for reusable functions and utilities.
    -   `test_cases/`: Individual test case scripts (e.g., `get_all_todos_test.groovy`, `add_todo_test.groovy`).
    -   `FakerUtils.groovy`: Utility class for generating fake data.
    -   `Utils.groovy`: General utility class for loading settings, JSON parsing, logging, and generic HTTP requests.
-   `tests/`: Main directory for all JMeter test plans.
    -   `test_plans/`: Main test plans that orchestrate the test flow.
        -   `dummyjson_orchestrator.jmx`: The main JMX file that executes the test cases from `scripts/test_cases/`.
-   `run_test.sh`: A convenience script to run JMeter tests from the command line.

## How to use

1.  **Download Java Faker:** Download `java-faker-1.0.2.jar` (or any other version) from Maven Central and place it in the `jmeter_project/lib_ext/` directory.
2.  **Configure JMeter Classpath:**
    *   Open `jmeter_project/tests/test_plans/dummyjson_orchestrator.jmx` in JMeter.
    *   Select the "Test Plan" element (the root element).
    *   In the "Test Plan" properties panel, locate the "User Defined Classpath" field.
    *   Add the following paths (relative to the `.jmx` file, or absolute paths if preferred):
        ```
        ../../lib_ext
        ../../scripts
        ../../scripts/test_cases
        ```
    *   This tells JMeter where to find your external JARs and custom Groovy scripts.
3.  **Configure Settings (Environment Variables & `settings.properties`):**
    *   The `Utils.getSetting()` method now prioritizes **environment variables** (e.g., `BASE_URL`, `PROTOCOL`) over values in `config/settings.properties`.
    *   **For sensitive data (e.g., API keys, passwords) or environment-specific configurations (e.g., `base_url` for different environments), it is highly recommended to use environment variables.**
    *   **How to set environment variables for JMeter:**
        *   **Linux/macOS (Bash/Zsh):**
            ```bash
            export BASE_URL="your.production.url"
            export PROTOCOL="https"
            # For passwords or API keys
            export API_KEY="your_secret_api_key"
            jmeter -n -t your_test_plan.jmx -l results.jtl
            ```
        *   **Windows (Command Prompt):**
            ```cmd
            set BASE_URL=your.production.url
            set PROTOCOL=https
            set API_KEY=your_secret_api_key
            jmeter -n -t your_test_plan.jmx -l results.jtl
            ```
        *   **Windows (PowerShell):**
            ```powershell
            $env:BASE_URL="your.production.url"
            $env:PROTOCOL="https"
            $env:API_KEY="your_secret_api_key"
            jmeter -n -t your_test_plan.jmx -l results.jtl
            ```
    *   `config/settings.properties` can serve as a fallback for default values or less sensitive configurations.
4.  **Configure Thread Group Properties (Users, Ramp-up, Loops):**
    *   These are defined as User Defined Variables (UDVs) directly in `dummyjson_orchestrator.jmx` at the Test Plan level.
    *   You can modify `NUM_THREADS`, `RAMP_TIME`, and `LOOPS` values in the "User Defined Variables" element within the Test Plan in JMeter GUI, or override them from the command line using `-J` flag with `run_test.sh`.
    *   Example to run with 10 users, 5s ramp-up, 2 loops:
        ```bash
        ./run_test.sh dummyjson_orchestrator -JNUM_THREADS=10 -JRAMP_TIME=5 -JLOOPS=2
        ```
5.  **Run Tests:** Execute `dummyjson_orchestrator.jmx` as usual.

## Writing Test Logic in `scripts/test_cases/`

All your test steps are now defined in individual Groovy scripts within `jmeter_project/scripts/test_cases/`. You can edit these files directly using your preferred code editor.

### `scripts/test_cases/` (Your Test Files)

Each `.groovy` file in this directory represents a test case. These files will directly use `FakerUtils` and `Utils` methods, including `makeHttpRequest`. You no longer need to import `groovy.net.http.Method.*` in these files, as it's handled by `Utils.groovy`.

**Example: `scripts/test_cases/add_todo_test.groovy`**

```groovy
// Simulate test.step
log.info("Test Step: Adding new todo...");

def randomTodo = getRandomProductName();
def randomUID = getRandomName().hashCode().abs() % 1000 + 1;
def randomBool = (getRandomEmail().hashCode() % 2 == 0);

def requestBody = [
    "todo": randomTodo,
    "userId": randomUID,
    "completed": randomBool
];

def headers = [
    "Content-Type": "application/json"
];

makeHttpRequest(POST, "/todos/add", requestBody, headers);

vars.put("randomTodo", randomTodo);
vars.put("randomUID", randomUID.toString());
vars.put("randomBool", randomBool.toString());

log.info("Test Step: Finished adding new todo.");
```

### `scripts/FakerUtils.groovy`

Provides static methods to generate various types of fake data.

```groovy
import static FakerUtils.getRandomProductName;
import static FakerUtils.getRandomEmail;

String productName = getRandomProductName();
vars.put("myProductName", productName);
```

### `scripts/Utils.groovy`

A general utility class for common tasks, including loading settings, JSON parsing, logging, and generic HTTP requests.

**Loading Settings:**
`Utils.groovy` automatically loads `settings.properties` and prioritizes environment variables. You can access settings like this:

```groovy
import static Utils.getSetting;

String baseUrl = getSetting("base_url"); // Will check ENV: BASE_URL first, then settings.properties
String protocol = getSetting("protocol"); // Will check ENV: PROTOCOL first, then settings.properties
log.info("Base URL: " + baseUrl + ", Protocol: " + protocol);
```

**JSON Parsing:**

```groovy
import static Utils.parseJson;

def jsonResponse = prev.getResponseDataAsString();
def parsedJson = parseJson(jsonResponse);
String extractedValue = parsedJson.some.path.to.value;
vars.put("extractedVar", extractedValue);
```

**Logging:**

```groovy
import static Utils.logInfo;
import static Utils.logError;

logInfo("This is an informational message.");
logError("This is an error message.");
```

**Generic HTTP Requests (`makeHttpRequest`):**

This method centralizes HTTP request logic, reducing repetition. It's called directly from your test case scripts.

```groovy
import static Utils.makeHttpRequest;
// No need to import groovy.net.http.Method.* here, it's handled by Utils.groovy

// Example GET request (in get_all_todos_test.groovy)
// makeHttpRequest(GET, "/todos");

// Example POST request with JSON body and custom headers (in add_todo_test.groovy)
// def requestBody = [
//     "todo": "Buy groceries",
//     "userId": 1,
//     "completed": false
// ];
// def headers = [
//     "Content-Type": "application/json",
//     "Authorization": "Bearer ${getSetting('API_KEY')}" // Example using an environment variable for API_KEY
// ];
// makeHttpRequest(POST, "/todos/add", requestBody, headers);
```