#!/bin/bash

# This script simplifies running JMeter tests from the command line.
# Usage: ./run_test.sh <test_plan_name> [options]
# Example: ./run_test.sh dummyjson_orchestrator -l results.jtl -e -o reports/html_report

# Get the directory where the script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

# Change to the script's directory (which is jmeter_project/)
cd "$SCRIPT_DIR" || exit

# Check if JMeter is installed and in PATH
if ! command -v jmeter &> /dev/null
then
    echo "JMeter is not found in your PATH. Please ensure JMeter is installed and accessible."
    exit 1
fi

# Check if a test plan name is provided
if [ -z "$1" ]; then
    echo "Usage: $0 <test_plan_name> [options]"
    echo "Example: $0 dummyjson_orchestrator -l results.jtl -e -o reports/html_report"
    echo "Available test plans in tests/test_plans/:"
    ls tests/test_plans/*.jmx | xargs -n 1 basename
    exit 1
fi

TEST_PLAN_NAME="$1"
TEST_PLAN_PATH="tests/test_plans/${TEST_PLAN_NAME}.jmx"

# Shift arguments so JMeter options can be passed directly
shift

# Check if the test plan exists
if [ ! -f "$TEST_PLAN_PATH" ]; then
    echo "Error: Test plan '$TEST_PLAN_PATH' not found."
    echo "Please ensure the test plan name is correct and the file exists in tests/test_plans/."
    exit 1
fi

echo "Running JMeter test plan: ${TEST_PLAN_PATH}"
echo "JMeter options: $@"

# Execute JMeter in non-GUI mode
# -n: Non-GUI mode
# -t: Test file
# -l: JTL results file (optional, can be passed as an argument)
# -e: Generate report after test (optional)
# -o: Output folder for report (optional)
jmeter -n -t "${TEST_PLAN_PATH}" "$@"