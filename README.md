
# JMeter Load Testing Project

This project contains JMeter test plans for load testing various endpoints.

## Prerequisites

1.  **Java**: Ensure you have Java Development Kit (JDK) 8 or higher installed.
2.  **Apache JMeter**: See installation instructions below.
3.  **Custom Libraries**: This project requires third-party libraries. Please follow the instructions in the `Lib/README.md` file to install them correctly before running any tests.

## JMeter Installation

### macOS

#### Recommended: Homebrew

The easiest way to install JMeter on macOS is using [Homebrew](https://brew.sh/).

```bash
brew install jmeter
```

This will automatically handle dependencies and place the `jmeter` command in your PATH. The `lib/ext` directory for custom libraries will typically be at `/usr/local/Cellar/jmeter/[VERSION]/libexec/lib/ext`.

#### Manual Installation

1.  Download the latest `apache-jmeter-x.x.x.tgz` file from the [JMeter Download page](https://jmeter.apache.org/download_jmeter.cgi).
2.  Unzip the file to a location of your choice (e.g., `~/apache-jmeter-5.6.3`).
3.  To run JMeter, you will need to use the full path to the `jmeter` script in the `bin` directory (e.g., `~/apache-jmeter-5.6.3/bin/jmeter`).

### Windows

#### Recommended: Winget

The easiest way to install JMeter on Windows is using the [Windows Package Manager (winget)](https://learn.microsoft.com/en-us/windows/package-manager/winget/).

```powershell
winget install -e --id Apache.JMeter
```

#### Manual Installation

1.  Download the latest `apache-jmeter-x.x.x.zip` file from the [JMeter Download page](https://jmeter.apache.org/download_jmeter.cgi).
2.  Unzip the file to a location of your choice (e.g., `C:\apache-jmeter-5.6.3`).
3.  To run JMeter, you will need to use the `jmeter.bat` script located in the `bin` directory (e.g., `C:\apache-jmeter-5.6.3\bin\jmeter.bat`).

## How to Run a Load Test

It is **highly recommended** to run JMeter in non-GUI (command-line) mode for executing load tests. The GUI mode should only be used for creating and debugging test scripts.

### Command-Line Mode (Recommended)

1.  Open your terminal (macOS/Linux) or command prompt (Windows).
2.  Navigate to the root directory of this project (`/Users/hendra/Code/Jmeter`).
3.  Execute the following command:

    ```bash
    jmeter -n -t [test_plan.jmx] -l [results_file.jtl]
    ```

    - `-n`: Specifies non-GUI mode.
    - `-t [test_plan.jmx]`: The path to the test plan file you want to run.
    - `-l [results_file.jtl]`: The path to the results file where JMeter will store the test run data.

    **Note:** The command above assumes you have installed JMeter using a package manager like Homebrew (macOS) or Winget (Windows), which adds the `jmeter` command to your system's PATH. If you installed JMeter manually, you must use the full path to the executable. See the examples below.

#### Available Test Plans

-   `dummyjson.jmx`
-   `AG-IT.jmx`
-   `AG-IT x-header.jmx`

---

### Examples

#### Recommended (Homebrew / Winget)

If you installed JMeter using a package manager, the `jmeter` command should be available globally.

```bash
# Example using the dummyjson.jmx test plan
jmeter -n -t dummyjson.jmx -l results/run1.jtl
```

#### Manual Installation

If you downloaded and unzipped JMeter manually, you must provide the full path to the `jmeter` executable inside the `bin` folder of your JMeter installation directory.

**macOS:**
```bash
# Replace with the actual path to your JMeter installation
/path/to/your/apache-jmeter-5.6.3/bin/jmeter -n -t dummyjson.jmx -l results/run1.jtl
```

**Windows:**
```bash
# Replace with the actual path to your JMeter installation
C:\path\to\your\apache-jmeter-5.6.3\bin\jmeter.bat -n -t dummyjson.jmx -l results\run1.jtl
```

## Analyzing Results

1.  Open the Apache JMeter GUI.
2.  Add a listener to your Test Plan (e.g., "View Results Tree" or "Summary Report").
3.  In the listener's configuration panel, click the "Browse..." button next to the "Filename" field.
4.  Open your `.jtl` results file (e.g., `results/run1.jtl`) to load and visualize the data.


