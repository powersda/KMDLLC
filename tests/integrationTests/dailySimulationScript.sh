#!/usr/bin/env bash

# This script simulates several daily login sessions to produce a valid daily transaction file.
# A backend executable is then run to process to day's daily transaction and create new input files for 
# USAGE: ./runDailyScript {number of sessions to simulate, defaults to 15}

SCRIPT_DIR="$(dirname "$(realpath "$0")")"                 # Directory that the current script resides in 
EXE_LOCATION="$SCRIPT_DIR""/../../OT-Bnb.jar"              # Location of the OT-Bnb executable, relative to the current script
USERS_FILE="$SCRIPT_DIR""/data/users.txt"                  # Location of the user.txt file relative to this script
LISTINGS_FILE="$SCRIPT_DIR""/data/listings.txt"            # Location of the listings.txt file relative tot his scripts
LOG_DIRECTORY="$SCRIPT_DIR""/log/"                         # Location of the directory to write daily transaction files, relative to this script
DEFAULT_SESSION_NUM=15                                     # Default number of sessions to run if no argument is passed to this script
NEW_USER_PREFIX="TEST"                                     # Prefix used when generating new usernames for new users
NEW_CITY_NAME="TESTCITY"                                   # City name used for new listings 

# Load users.txt
[ ! -f "$USERS_FILE" ] && printf "users.txt not found!\n" && exit 1
printf "Reading in users from users.txt...\n"
readarray users < "$USERS_FILE"
unset users[-1]
for user in "${users[@]}"; do
    printf "Read in user: ""$user"
done

# Load listings.txt
[ ! -f "$LISTINGS_FILE" ] && printf "listings.txt not found!\n" && exit 1
printf "\nReading in listings from listings.txt...\n"
readarray listings < "$LISTINGS_FILE"
unset listings[-1]
for listing in "${listings[@]}"; do
    printf "Read in listing: ""$listing"
done

# Make sure log directory exists
[ ! -d "$LOG_DIRECTORY" ] && printf "Log directory does not exist!\n" && exit 1

# Functions to retrieve user/listing fields (using xargs to strip whitespace)
function usrPrintUsername() { printf "${users["$1"]:0:15}" | xargs; }
function usrPrintUsertype() { printf "${users["$1"]:16:2}" | xargs; }
function lstPrintUnitID()   { printf "${listings["$1"]:0:8}" | xargs; }
function lstPrintUsername() { printf "${listings["$1"]:9:15}" | xargs; }
function lstPrintCity()     { printf "${listings["$1"]:25:25}" | xargs; }
function lstPrintPrice()    { printf "${listings["$1"]:53:6}" | xargs; }
function lstPrintRooms()    { printf "${listings["$1"]:51:1}" | xargs; }
function lstPrintRented()   { printf "${listings["$1"]:60:1}" | xargs; }
function lstPrintNights()   { printf "${listings["$1"]:62:2}" | xargs; }

# Create input to simulate the creation of a new user
function create() {
    # NOTE: new users are not managed by this script, so test input will not be generated with them as the active user
    # Get new user number; if unset then 1, else 1 greater than previous new user number
    [ -z "${newUserNum+set}" ] && newUserNum=1 || let newUserNum++

    #  Generate input to create user
    input+="login\r""$(usrPrintUsername "$1")""\rcreate\r""$NEW_USER_PREFIX""$newUserNum""\rAA\rlogout\r"
    printf "Input generated to create new user: \"""$NEW_USER_PREFIX""$newUserNum""\"!\n"
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
    input+="login\r""$(usrPrintUsername "$1")""\rdelete\r""$(usrPrintUsername "$userIndex")""\rlogout\r"
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
}

function post() {
    # Generate input to post a new city with a fixed name, random price, and random number of bedrooms within allowable ranges
    input+="login\r""$(usrPrintUsername "$1")""\rpost\r""$NEW_CITY_NAME""\r""$(( "$RANDOM" % 999 + 1 ))""\r""$(( "$RANDOM" % 9 + 1 ))""\rlogout\r"
    printf "Input generated to post new listing in city \"""$NEW_CITY_NAME""\"!\n" 
}

function search() {
     # Pick a listing to search for. NOTE: may search for listings that are rented- this is okay!
     local listingIndex="$(( "$RANDOM" % "${#listings[@]}" ))"

     # Generate input to search for all listings in the above city
     input+="login\r""$(usrPrintUsername "$1")""\rsearch\r""$(lstPrintCity "$listingIndex")""\r*\r*\rlogout\r"
     printf "Input generated to search for listings in city \"""$(lstPrintCity "$listingIndex")""\"!\n"
}
function rent() {
     # Pick a listing to search for. NOTE: listing may already be rented- this is okay!
     local listingIndex="$(( "$RANDOM" % "${#listings[@]}" ))"

     # Update internal listings array to reflect rented status (may already be) for delete(). NOTE: number of nights not updated
     listings["$listingIndex"]="${listings["$listingIndex"]/" F "/" T "}"

     # Generate input to attempt to rent listing at above index
     input+="login\r""$(usrPrintUsername "$1")""\rrent\r""$(lstPrintUnitID "$listingIndex")""\r""$(( "$RANDOM" % 14 + 1 ))""\ryes\rlogout\r"
     printf "Input generated to attempt to rent listing \"""$(lstPrintUnitID "$listingIndex")""\"!\n"
}

# numberOfSessions="$(( "$RANDOM" % ("$MAX_DAILY_SESSIONS" - "$MIN_DAILY_SESSIONS" + 1) + "$MIN_DAILY_SESSIONS" ))"
[[ "$1" =~ ^[0-9]+$ ]] && numberOfSessions="$1" || numberOfSessions="$DEFAULT_SESSION_NUM"
printf "\nSimulating input for ""$numberOfSessions"" sessions...\n"
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
    printf "Generating input for command \"""${availableCommands["$commandIndex"]}""\" as user \"""$(usrPrintUsername "$userIndex")""\" with user type \"""$(usrPrintUsertype "$userIndex")""\"...\n"
    "${availableCommands["$commandIndex"]}" $userIndex 
done

# Feed input to application
input+="quit\r"
printf "\nRunning application with generated input\n"
printf "$input" | java -jar "$EXE_LOCATION" "$LOG_DIRECTORY" "$USERS_FILE" "$LISTINGS_FILE" > /dev/null
printf "Simulation for ""$numberOfSessions"" session complete!\n"
exit 0



