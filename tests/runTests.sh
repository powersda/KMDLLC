#!/usr/bin/env bash

SCRIPT_DIR="$(dirname "$(realpath "$0")")"                 # Directory that the current script resides in 
EXE_LOCATION="$SCRIPT_DIR""/../OT-Bnb.jar"                  # Location of the OT-Bnb executable, relative to the current script
TEST_DIR="$SCRIPT_DIR""/functionalityTests"                # Directory containing units tests relative to current script
REPORT_DIR="$SCRIPT_DIR""/systemTestReports"               # Directory containing test reports to relative to current script

COMMAND_FILE="commands.txt"                                 # Location of test input commands relative to unit test directory
USERS_FILE="inputs/users.txt"                               # Location of users.txt file relative to unit test directory
LISTINGS_FILE="inputs/listings.txt"                         # Location of listings.txt file relative to unit test directory
EXPECTED_OUTPUTS_FILE="expectedOutputs/transactionFile.txt" # Location of expected outputs relative to unit test directory
TEST_RESULTS_DIR="testResults"                              # Directory that unit test results should be written to, relative to unit test directory

QUIT_CHAR="q"                                               # Input character required to cancel tests
RESULTS_SUFFIX="$(date +%Y_%m_%d_%H_%M_%S)"                 # Suffix used for naming report and diff files
REPORT_FILE=""$REPORT_DIR"/report_"$RESULTS_SUFFIX".txt"    # Name and location of the report file that will be created the current script

# Attempt to load tests (by checking if they have a command file)
for d in "$TEST_DIR"/*/*; do
    if [ ! -f "$d"/"$COMMAND_FILE" ]; then
        echo "Error loading "$(basename "$d")": "$COMMAND_FILE" missing"
    else
       loaded_tests+=("$d")
       echo "Test loaded:"$(basename "$d")"" 
    fi
done
echo -e "Loaded "${#loaded_tests[@]}" tests successfully!\n"

# Get user input
tests_to_run=()
while [ "${#tests_to_run[@]}" -eq 0 ]; do
    read -a inputs -p "Enter the tests you wish to run, "$QUIT_CHAR" to quit, or press enter to test all: "

    # Check inputs
    if (("${#inputs[@]}")); then

        # Quit if input is the quit character
        [ "$inputs" = "$QUIT_CHAR" ] && exit 0

        # Get all tests for each input parameter; if any are invalid, reprompt the user for input 
        for input in "${inputs[@]}"; do
            parsed_tests=()
            for test in "${loaded_tests[@]}"; do
                [[ "$(basename "$test")" =~ "$input" ]] && parsed_tests+=("$test")
            done
            if [ "${#parsed_tests[@]}" -eq 0 ]; then
                echo "Test \""$input"\" not found!"
                tests_to_run=()
                continue 2
            fi
            tests_to_run+=("${parsed_tests[@]}")
        done

    # If no user input, select all loaded tests
    else
        tests_to_run=("${loaded_tests[@]}")
    fi
done

# Loop through all tests, using passing their input files to the program and comparing the created output file to the test's expected output
for test in "${tests_to_run[@]}"; do
    diff_file="$test"/"$TEST_RESULTS_DIR"/unit_"$RESULTS_SUFFIX".diff

    echo "Running "$test"..."
    java -jar "$EXE_LOCATION" "$test"/ "$test"/"$USERS_FILE" "$test"/"$LISTINGS_FILE" < "$test"/"$COMMAND_FILE" > /dev/null
    diff -ZyatW 142 --suppress-common-lines "$test"/*.log "$test"/"$EXPECTED_OUTPUTS_FILE" > $diff_file
    rm "$test"/*.log
    echo "Test results written to "$diff_file"" 

    # If the unit test failed, append the diff output to the report file
    {   if [ -s "$diff_file" ]; then
            echo -e "Test \""$(basename "$test")"\" failed. Details are as follows (right column is expected output):\n""$(cat "$diff_file")""\n"
        else 
            echo -e "Test \""$(basename "$test")"\" completed successfully!\n"
        fi
    } >> "$REPORT_FILE"

done

# Output an appropriate message to the report file if all tests were successful, and notify user of test completion
echo -e "\nTests Complete! Report file written to "$REPORT_FILE""
