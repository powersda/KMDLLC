/*******************************
* Class: RentListing
* Description: Handles Rent Listing Transactions and validates inputs.
* 			   Output should display all rent listing info, and confirmation if user enters yes or cancellation prompt if user enters no.
********************************/
import java.util.Scanner;

//Extends the State class to allow for users under a specific usertypes to rent existing non-rented listings.
public class RentListing extends State {

	// Implements the execute method from State to rent a valid listing
    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {
    	// Check user type before declaring variables.
        if (!Command.RENT.validateUser(activeUser))
                throw new SecurityException(activeUser == null? "You must be logged in before you can use this command!" : "Sorry, you don't have permission to use this command.");

     // Default variables set to null to prevent "variable not initialized" error in loops.
    	String input;
    	Listing listing = null;
    	String tempOwner = null;
    	int numberOfNights = 1; //default 1
    	double totalCost = 1; // default 1
    	// flags for input checking loops
    	boolean listingIDFlag = true;
    	boolean NightsFlag = true;
    	boolean confirmationFlag = true;
    	
    	
    	// Validates Listing ID and makes sure the Listing ID is Alphanumeric, contains exactly 8 characters, exists in the database, is not owned by current user, and is not rented.
        while (listingIDFlag) {
        	
        	try {
        		System.out.print("Enter the unit ID of the listing you would like to rent: ");
        		input = inputSource.nextLine().trim();
        		
        		// checks if the rental ID is 8 characters alphanumeric
        		if(!Listing.isValidRentalID(input)) {
        			throw new IllegalArgumentException();
        		}
        		// checks that the listing exists
        		else if(dbHandle.listingExists(input)){
        			listing = dbHandle.getListing(input);
        			
        			// check that listing is not owned by current user.
        			tempOwner = listing.getOwner().getUsername();
        			if(tempOwner.equals(activeUser.getUsername())) {
        				listingIDFlag = NightsFlag = confirmationFlag = false; //returns user back to main menu;
        				System.out.println("You cannot rent listings owned by you. Returning to main menu.");
        			}
        			// checks that the listing is not rented
        			else if(listing.isRented()) { 
        				listingIDFlag = NightsFlag = confirmationFlag = false; //returns user back to main menu;
        				System.out.println("Listing is already rented. Returning to main menu.");
        			}
        			else {
        				listingIDFlag = false;
        			}
        		}
        		else {
        			listingIDFlag = NightsFlag = confirmationFlag = false; //returns user back to main menu;
        			System.out.println("Unit ID not found. Returning to main menu.");
        		}
        	}
        	catch(Exception e) {
        		if(e instanceof IllegalArgumentException) {
        			System.out.println("Input invalid. Unit ID must be alphanumeric and exactly 8 characters.");
        		}
        		else {
        			System.out.println("An error occured, try again later.");
        		}
        	}
        }
        
        // Validates the number of nights input and makes sure that the input is int  between 1 - 14
        // Once number of nights input is correct, totalcost is calculated
        while(NightsFlag) {
        	try {
        		System.out.print("Enter the number of nights you'll be staying: ");
        		input = inputSource.nextLine().trim();
        		int nightsInput = Integer.parseInt(input);
        		
        		if(!Listing.isValidNightsRented(nightsInput)) {
        			throw new IllegalArgumentException();
        		}
        		else {
        			numberOfNights = nightsInput;
        			//calculate listing rental price * number of nights and round total cost to two decimal places.
        			totalCost = Math.round((nightsInput * listing.getRentalPrice())*100.0)/100.0; 
        			NightsFlag = false;
        		}
        	}
        	catch(Exception e) {
        		if(e instanceof NumberFormatException) {
        			System.out.println("Invalid Entry. Input must be an integer amount.");
        		}
        		else if(e instanceof IllegalArgumentException) {
        			System.out.println("Invalid Entry. You can only rent a listing for 1 - 14 nights.");
        		}
        		else {
        			System.out.println("An error occured, try again later.");
        		}
        		
        	}
        }
        
        // Validate confirmation input and make sure that input is: "yes", "Yes", "YES", "y", "Y" or "no", "No", "NO", "n", "N"
        // if user enters yes or y, then the listing will be set to rented and the nights rented will be added to the object,
        // a log of the transaction will be created and the user will be prompted about confirmation and returned to the main menu.
        // if user enter no or n, then the transaction will be cancelled and the user will be returned back to the main menu.
        // any other input is incorrect and will prompt the user of error and re-prompt the user to enter confirmation again.
        while(confirmationFlag) {
        	try {
        		System.out.print("Unit ID: " + listing.getRentalUnitID() + "\nNights Rented: " +
        							numberOfNights + "\nPrice Per Night: $" + listing.getRentalPrice() +
        							"\nTotal Cost: $" + totalCost + "\nEnter yes to confirm or no to cancel: ");
        		input = inputSource.nextLine().trim().toUpperCase(); //Switch to upper case to only check for YES/NO Y/N input.
        		if(input.equals("YES") || input.equals("Y")) {
        			listing.setNightsRented(numberOfNights);
        			listing.setRentedFlag(true);
        			dbHandle.addLog(new Log(Log.TransactionCode.RENT, activeUser, listing)); // adds log
        			confirmationFlag = false;
        			System.out.println("Transaction confirmed.");
        		}
        		else if(input.equals("NO") || input.equals("N")) {
        			// send user back to main menu.
        			confirmationFlag = false;
        			System.out.println("Transaction cancelled.");
        		}
        		else {
        			throw new IllegalArgumentException();
        		}
        	}
        	catch(Exception e) {
        		if(e instanceof IllegalArgumentException) {
        			System.out.println("Invalid Entry. You must enter either yes to confirm or no to cancel. ");
        		}
        		else {
        			System.out.println("An error occured, try again.");
        		}
        	}
        }
        return activeUser;
    }
}
