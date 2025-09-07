#!/bin/bash

# Script to run a JAR file with multiple input/output test pairs
# Usage: ./test_jar_batch.sh <jar_file> <input_folder> <output_folder> [java_args]

set -euo pipefail

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Test results tracking
declare -a passed_tests=()
declare -a failed_tests=()
total_tests=0

# Function to print usage
usage() {
    echo "Usage: $0 <jar_file> <input_folder> <output_folder> [java_args]"
    echo ""
    echo "Arguments:"
    echo "  jar_file       Path to the JAR executable"
    echo "  input_folder   Folder containing input files (e.g., 1-in.txt, 2-in.json)"
    echo "  output_folder  Folder containing expected output files (e.g., 1-out.txt, 2-out.json)"
    echo "  java_args      Optional Java arguments (e.g., -Xmx1024m)"
    echo ""
    echo "File naming convention:"
    echo "  Input files:  {index}-in.{ext}  (e.g., 1-in.txt, 2-in.json)"
    echo "  Output files: {index}-out.{ext} (e.g., 1-out.txt, 2-out.json)"
    echo ""
    echo "Examples:"
    echo "  $0 myapp.jar tests/inputs tests/outputs"
    echo "  $0 myapp.jar test_data/in test_data/out '-Xmx512m'"
    exit 1
}

# Function to check if directory exists and is readable
check_directory() {
    local dir="$1"
    local description="$2"

    if [[ ! -d "$dir" ]]; then
        echo -e "${RED}Error: $description '$dir' does not exist or is not a directory${NC}" >&2
        exit 1
    fi

    if [[ ! -r "$dir" ]]; then
        echo -e "${RED}Error: $description '$dir' is not readable${NC}" >&2
        exit 1
    fi
}

# Function to check if file exists and is readable
check_file() {
    local file="$1"
    local description="$2"

    if [[ ! -f "$file" ]]; then
        echo -e "${RED}Error: $description '$file' does not exist${NC}" >&2
        return 1
    fi

    if [[ ! -r "$file" ]]; then
        echo -e "${RED}Error: $description '$file' is not readable${NC}" >&2
        return 1
    fi

    return 0
}

# Function to extract test index from filename
get_test_index() {
    local filename="$1"
    local basename=$(basename "$filename")

    # Extract number before "-in" or "-out"
    if [[ $basename =~ ^([0-9]+)-(in|out)\. ]]; then
        echo "${BASH_REMATCH[1]}"
    else
        echo ""
    fi
}

# Function to get file extension
get_extension() {
    local filename="$1"
    echo "${filename##*.}"
}

# Function to find matching output file for input file
find_matching_output() {
    local input_file="$1"
    local output_folder="$2"

    local test_index=$(get_test_index "$input_file")
    local extension=$(get_extension "$input_file")

    if [[ -z "$test_index" ]]; then
        return 1
    fi

    # Try same extension first
    local output_file="$output_folder/${test_index}-out.${extension}"
    if [[ -f "$output_file" ]]; then
        echo "$output_file"
        return 0
    fi

    # Try other common extensions
    for ext in txt json xml csv; do
        output_file="$output_folder/${test_index}-out.${ext}"
        if [[ -f "$output_file" ]]; then
            echo "$output_file"
            return 0
        fi
    done

    return 1
}

# Function to run a single test
run_single_test() {
    local jar_file="$1"
    local input_file="$2"
    local expected_output_file="$3"
    local java_args="$4"
    local test_index="$5"

    local actual_output=$(mktemp)
    local test_name="Test $test_index ($(basename "$input_file"))"

    echo -e "${BLUE}Running $test_name...${NC}"

    # Execute JAR with input and capture output
    if java $java_args -jar "$jar_file" < "$input_file" > "$actual_output" 2>&1; then
        # Compare outputs
        if diff -q "$expected_output_file" "$actual_output" > /dev/null; then
            echo -e "${GREEN}✓ $test_name PASSED${NC}"
            passed_tests+=("$test_index")
            rm -f "$actual_output"
            return 0
        else
            echo -e "${RED}✗ $test_name FAILED - Output differs${NC}"
            failed_tests+=("$test_index")

            # Show diff for failed test
            echo "  Expected vs Actual diff:"
            diff -u "$expected_output_file" "$actual_output" | head -20 | sed 's/^/    /'
            if [[ $(wc -l < <(diff -u "$expected_output_file" "$actual_output")) -gt 20 ]]; then
                echo "    ... (truncated, full diff saved)"
            fi

            # Save actual output for debugging
            local saved_output="failed_${test_index}_actual_output.txt"
            cp "$actual_output" "$saved_output"
            echo -e "  ${YELLOW}Actual output saved to: $saved_output${NC}"
            rm -f "$actual_output"
            return 1
        fi
    else
        local exit_code=$?
        echo -e "${RED}✗ $test_name FAILED - JAR execution failed (exit code: $exit_code)${NC}"
        echo "  Error output:"
        cat "$actual_output" | sed 's/^/    /'
        failed_tests+=("$test_index")
        rm -f "$actual_output"
        return 1
    fi
}

# Function to print test summary
print_summary() {
    echo ""
    echo "========================================"
    echo -e "${BLUE}TEST SUMMARY${NC}"
    echo "========================================"
    echo "Total tests: $total_tests"
    echo -e "${GREEN}Passed: ${#passed_tests[@]}${NC}"
    echo -e "${RED}Failed: ${#failed_tests[@]}${NC}"

    if [[ ${#failed_tests[@]} -gt 0 ]]; then
        echo ""
        echo -e "${RED}Failed tests:${NC}"
        for test_index in "${failed_tests[@]}"; do
            echo "  - Test $test_index"
        done
    fi

    if [[ ${#passed_tests[@]} -gt 0 ]]; then
        echo ""
        echo -e "${GREEN}Passed tests:${NC}"
        for test_index in "${passed_tests[@]}"; do
            echo "  - Test $test_index"
        done
    fi

    echo ""
    if [[ ${#failed_tests[@]} -eq 0 ]]; then
        echo -e "${GREEN}🎉 All tests passed!${NC}"
        return 0
    else
        echo -e "${RED}❌ ${#failed_tests[@]} test(s) failed${NC}"
        return 1
    fi
}

# Main script starts here

# Check arguments
if [[ $# -lt 3 || $# -gt 4 ]]; then
    echo -e "${RED}Error: Invalid number of arguments${NC}" >&2
    usage
fi

# Parse arguments
jar_file="$1"
input_folder="$2"
output_folder="$3"
java_args="${4:-}"

# Validate inputs
check_file "$jar_file" "JAR file"
check_directory "$input_folder" "Input folder"
check_directory "$output_folder" "Output folder"

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo -e "${RED}Error: Java is not installed or not in PATH${NC}" >&2
    exit 1
fi

echo -e "${YELLOW}Starting batch test execution...${NC}"
echo "JAR file: $jar_file"
echo "Input folder: $input_folder"
echo "Output folder: $output_folder"
echo "Java args: ${java_args:-none}"
echo ""

# Find all input files and sort them numerically
input_files=()
while IFS= read -r -d '' file; do
    if get_test_index "$file" > /dev/null; then
        input_files+=("$file")
    fi
done < <(find "$input_folder" -name "*-in.*" -type f -print0)

# Sort files by test index
IFS=$'\n' input_files=($(sort -t- -k1,1n <<<"${input_files[*]}"))

if [[ ${#input_files[@]} -eq 0 ]]; then
    echo -e "${RED}Error: No input files found matching pattern '*-in.*' in $input_folder${NC}" >&2
    exit 1
fi

echo -e "${BLUE}Found ${#input_files[@]} input file(s)${NC}"

# Process each input file
for input_file in "${input_files[@]}"; do
    if ! output_file=$(find_matching_output "$input_file" "$output_folder"); then
        test_index=$(get_test_index "$input_file")
        echo -e "${YELLOW}⚠ Skipping test $test_index - no matching output file found${NC}"
        continue
    fi

    if ! check_file "$output_file" "Expected output file"; then
        continue
    fi

    test_index=$(get_test_index "$input_file")
    ((total_tests++))

    run_single_test "$jar_file" "$input_file" "$output_file" "$java_args" "$test_index"
done

# Print final summary
print_summary