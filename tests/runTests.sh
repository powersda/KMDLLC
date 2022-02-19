#!/usr/bin/env bash

COMMANDFILE="commands.txt"
TESTDIR="unitTests/*/*"
TESTRESULTSDIR="testResults/"
REPORTDIR="systemTestReports/"
QUITCHAR="q"

# Attempt to load tests
for d in $TESTDIR; do
    if [ ! -f ""$d"/"$COMMANDFILE"" ]; then
        echo "Error loading "$(basename "$d")": commands.txt missing"
    else
       LOADEDTESTS+=("$d")
       echo "Test loaded:"$(basename "$d")"" 
    fi
done
echo -e "Loaded "${#LOADEDTESTS[@]}" tests successfully!\n"

# Get user input
BADINPUT=true
while $BADINPUT; do
    RUNNINGTESTS=()
    read -a INPUTS -p "Enter the tests you wish to run, "$QUITCHAR" to quit, or press enter to test all: "

    # Check inputs
    if (("${#INPUTS[@]}")); then

        # Quit if input is the quit character
        [ "$INPUTS" = "$QUITCHAR" ] && exit 0

        # Make sure inputs are valid tests
        for INPUT in "${INPUTS[@]}"; do
            [[ ! "${LOADEDTESTS[@]}" =~ "$INPUT" ]] && echo "Test \""$INPUT"\" not found!" && continue 2
            RUNNINGTESTS+=("$INPUT")
        done

    # Select all loaded tests
    else
        RUNNINGTESTS=("${LOADEDTESTS[@]}")
    fi
    BADINPUT=false
done

# Run Tests
# Backup /data and /log
REPORTSUFFIX="$(date +%Y_%m_%d_%H_%M_%S)"
for TEST in "${RUNNINGTESTS[@]}"; do
    echo "Running "$TEST"..."
    # copy test inputs
    # run program with command.txt input
    # compare output to expectedOutput
    # log results in local resultsFolder
    # delete test output
    echo "Test results file \"unit_"$REPORTSUFFIX"\" generated in "$TEST"/"$TESTRESULTSDIR""
done
# Restore /data and /log
# Create master test report
echo -e "\nReport file \"report_"$REPORTSUFFIX"\" generated in "$REPORTDIR""
