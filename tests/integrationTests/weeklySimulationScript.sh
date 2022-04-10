#!/usr/bin/env bash

# This script simulates the use of OT-Bnb.jar over 5 days. For each day, dailySimulationScript is run to generate a daily transaction file.
# Next, BackendStub.class is run to process the daily transaction file and produce a new users.txt and listings.txt file for the following day.
# USAGE: ./weeklySimulationScript {number of days to simulate, default 5} {number of sessions per day to simulate, default 15}

SCRIPT_DIR="$(dirname "$(realpath "$0")")"                  # Directory that the current script resides in 
BACKEND_EXE="$SCRIPT_DIR""/BackendStub"                     # Location of the backend executable, relative to this script
DAILY_SCRIPT="$SCRIPT_DIR""/dailySimulationScript.sh"       # Location of the daily simulation script, relative to this script
USERS_FILE="$SCRIPT_DIR""/data/users.txt"                   # Location of the user.txt file relative to this script
LISTINGS_FILE="$SCRIPT_DIR""/data/listings.txt"             # Location of the listings.txt file relative tot his scripts
LOG_DIRECTORY="$SCRIPT_DIR""/log/"                          # Location of the directory to write daily transaction files, relative to this script
DEFAULT_NUM_OF_DAYS=5                                       # Default number of days to simulate if no parameter is received
DEFAULT_NUM_OF_SESSIONS=15                                  # Default number of sessions per day to simulate if no parameter is received

# Set script parameters based on passed arguments, otherwise use defaults
numberOfDays="$([ ! -z "$1" ] && printf "$1" || printf "$DEFAULT_NUM_OF_DAYS")"
numberOfSessions="$([ ! -z "$2" ] && printf "$2" || printf "$DEFAULT_NUM_OF_SESSIONS")"

printf "\nBeginning integration test: simulating ""$numberOfSessions"" sessions per day for ""$numberOfDays"" days\n"
echo $SCRIPT_DIR

for day in $(seq 1 "$numberOfDays"); do
    printf "\nSimulating day ""$day""...\n" 
    
    # Run daily script for current day
    "$DAILY_SCRIPT" "$numberOfSessions" > /dev/null
    printf "Simulation complete. Running backend to process daily transaction file...\n"

    # Run backend for current day
    java -cp "$SCRIPT_DIR""/../../src:""$SCRIPT_DIR" "$(basename "$BACKEND_EXE")" "$LOG_DIRECTORY""$(date '+%Y_%m_%d')"".log" "$USERS_FILE" "$LISTINGS_FILE"
    printf "Backend processing complete. Archiving daily transaction file...\n"

    # Change name of daily transaction file so it doesn't conflict with the next day's output
    mv "$LOG_DIRECTORY""$(date '+%Y_%m_%d')"".log" "$LOG_DIRECTORY""$(date --date=""$(($numberOfDays - $day))" days ago" '+%Y_%m_%d')"".log"  
done

