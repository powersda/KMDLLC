#!/usr/bin/env bash

# This script simulates several daily login sessions to produce a valid daily transaction file.
# A backend executable is then run to process to day's daily transaction and create new input files for the following day
# USAGE: ./runDailyScript {number of sessions to simulate, defaults to 15}

SCRIPT_DIR="$(dirname "$(realpath "$0")")"                 # Directory that the current script resides in 
EXE_LOCATION="$SCRIPT_DIR""/../../OT-Bnb.jar"              # Location of the OT-Bnb executable, relative to the current script
USERS_FILE="$SCRIPT_DIR""/data/users.txt"                  # Location of the user.txt file relative to this script
LISTINGS_FILE="$SCRIPT_DIR""/data/listings.txt"            # Location of the listings.txt file relative tot his scripts
LOG_DIRECTORY="$SCRIPT_DIR""/log/"                         # Location of the directory to write daily transaction files, relative to this script
DEFAULT_SESSION_NUM=15                                     # Default number of sessions to run if no argument is passed to this script

# Load users.txt
printf "Reading in users from users.txt...\n"
readarray users < "$USERS_FILE"
unset users[-1]
for user in "${users[@]}"; do
    printf "Read in user: ""$user"
done

# Load listings.txt
printf "\nReading in listings from listings.txt...\n"
readarray listings < "$LISTINGS_FILE"
unset listings[-1]
for listing in "${listings[@]}"; do
    printf "Read in listing: ""$listing"
done

# Functions to retrieve user/listing fields (using xargs to strip whitespace)
function usrPrintUsername() { printf "${users["$1"]:0:15}" | xargs printf; }
function usrPrintUsertype() { printf "${users["$1"]:16:2}" | xargs printf; }
function lstPrintUnitID()   { printf "${listings["$1"]:0:8}" | xargs printf; }
function lstPrintUsername() { printf "${listings["$1"]:9:15}" | xargs printf; }
function lstPrintCity()     { printf "${listings["$1"]:25:25}" | xargs printf; }
function lstPrintPrice()    { printf "${listings["$1"]:53:6}" | xargs printf; }
function lstPrintRooms()    { printf "${listings["$1"]:51:1}" | xargs printf; }
function lstPrintRented()   { printf "${listings["$1"]:60:1}" | xargs printf; }
function lstPrintNights()   { printf "${listings["$1"]:62:2}" | xargs printf; }

# Create input to simulate the creation of a new user
function create() {
    # Get new user number; if unset then 1, else 1 greater than previous new user number
    [ -z "${newUserNum+set}" ] && newUserNum=1 || let newUserNum++

    #  Generate input to create user
    local newUserPrefix="TEST"
    input="login\r""$(usrPrintUsername "$1")""\rcreate\r""$newUserPrefix""$newUserNum""\rAA\rlogout\rquit"

    # TODO: add newly created user to internally managed user list so subsequent tests can use them

    printf "Input generated to create new user: \"""$newUserPrefix""$newUserNum""\"!\n"
}

# Create input to simulate the deletion of an existing user
function delete() {

    while :; do
        # Pick a user to delete. 
        local userIndex="$(( "$RANDOM" % "${#users[@]}" ))"

        # If the index of the current user is chosen, or chosen user is an admin, pick another user
        if [ "$1" -eq "$userIndex" ] || [ "$(usrPrintUsertype "$userIndex")" = "AA" ]; then 
            printf "Cannot delete \"""$(usrPrintUsername "$userIndex")""\"; they are the active user or an admin!\n"
            continue
        fi

        # If the chosen user has an open listing, pick another user 
        for listingIndex in $(seq 0 "$(("${#listings[@]}" - 1))"); do
            if [ "$(lstPrintUsername "$listingIndex")" = "$(usrPrintUsername "$userIndex")" ] && [ "$(lstPrintRented "$listingIndex")" == "T" ]; then
                printf "Cannot delete \"""$(usrPrintUsername "$userIndex")""\"; they have an active listing!\n"
                continue 2;
            fi
        done

        break;
    done
    
    # Generate input to delete user
    input="login\r""$(usrPrintUsername "$1")""\rdelete\r""$(usrPrintUsername "$userIndex")""\rlogout\rquit\r"
    printf "Input generated to delete user: \"""$(usrPrintUsername "$userIndex")""\"!\n"

    # Remove listings associated with user from internally managed listings array
    for listingIndex in $(seq 0 "$(("${#listings[@]}" - 1))"); do
        if [ "$(lstPrintUsername "$listingIndex")" = "$(usrPrintUsername "$userIndex")" ]; then
            unset listings["$listingIndex"] 
        fi
    done
    listings=( "${listings[@]}" )

    # Remove user from internally managed users array
    unset users["$userIndex"];
    users=( "${users[@]}" )

    printf "$input" | java -jar "$EXE_LOCATION" "$LOG_DIRECTORY" "$USERS_FILE" "$LISTINGS_FILE" > /dev/null

}
function post() {

    local newCityName="TEST"

    input="login\r""$(usrPrintUsername "$1")""\rpost\r""$newCityName""\r""$(( "$RANDOM" % 999 + 1 ))""\r""$(( "$RANDOM" % 9 + 1 ))""\rAA\rlogout\rquit"
    printf "Input generated to post new listing in city \"""$newCityName""\"!\n" 
    printf "$input" | java -jar "$EXE_LOCATION" "$LOG_DIRECTORY" "$USERS_FILE" "$LISTINGS_FILE" > /dev/null
}
function search() {


}
function rent() { echo "rent"; }

# numberOfSessions="$(( "$RANDOM" % ("$MAX_DAILY_SESSIONS" - "$MIN_DAILY_SESSIONS" + 1) + "$MIN_DAILY_SESSIONS" ))"
[[ "$1" =~ ^[0-9]+$ ]] && numberOfSessions="$1" || numberOfSessions="$DEFAULT_SESSION_NUM"
printf "\nSimulating ""$numberOfSessions"" sessions...\n"
for session in $(seq 1 "$numberOfSessions"); do

    #Pick a random user index, then determine the commands the corresponding user can run
    userIndex="$(( "$RANDOM" % "${#users[@]}" ))"
    case "$(usrPrintUsertype "$userIndex")" in
        "AA")
            availableCommands=(create delete post search rent)
            ;;
        "FS")
            availableCommands=(post search rent)
            ;;
        "PS")
            availableCommands=(post search)
            ;;
        "RS")
            availableCommands=(search rent)
            ;;
    esac

    # Generate input based on a randomly chosen command
    commandIndex="$(( "$RANDOM" % "${#availableCommands[@]}" ))"
    printf "Running command \"""${availableCommands["$commandIndex"]}""\" as user \"""$(usrPrintUsername "$userIndex")""\" with user type \"""$(usrPrintUsertype "$userIndex")""\"...\n"
    "${availableCommands["$commandIndex"]}" $userIndex 


    # Feed input to application
    # printf "$input" | java -jar "$EXE_LOCATION" "$LOG_DIRECTORY" "$USERS_FILE" "$LISTINGS_FILE"
done


