import java.util.Scanner;

public class RentListing extends State {

    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {
    	String input;
    	Listing listing = null;
    	int numberOfNights = 1; //default 1
    	double totalCost = 1; // default 1
    	boolean listingIDFlag = true;
    	boolean NightsFlag = true;
    	boolean confirmationFlag = true;
    	
        if (!Command.RENT.validateUser(activeUser))
                throw new SecurityException(activeUser == null? "You must be logged in before you can use this command!" : "Sorry, you don't have permission to use this command.");

        //TODO: this is tester must move to a loop that kicks user out of menu if id input is incorrect
        //TODO: Add log.
        while (listingIDFlag) {
        	try {
        		System.out.print("\nEnter the ID of the listing you would like to rent: ");
        		input = inputSource.nextLine();
        		
        		if(!Listing.isValidRentalID(input)) {
        			throw new IllegalArgumentException();
        		}
        		else if(dbHandle.listingExists(input)){
        			listing = dbHandle.getListing(input);
        			if(listing.isRented()) {
            			//not complete kick user out of rent listing menu
        				System.out.print("\nListing is not available for rental. Try another.");
        			}
        			else {
        				listingIDFlag = false;
        			}
        		}
        		else {
        			System.out.print("\nListing id not found");
        		}
        	}
        	catch(Exception e) {
        		if(e instanceof IllegalArgumentException) {
        			System.out.print("\nInput Invalid. Listing ID must be alpha numeric and exactly 8 characters.");
        		}
        		else {
        			System.out.print("\nAn error occured, try again later.");
        		}
        	}
        }
        
        //Enter how many nights the user will stay
        while(NightsFlag) {
        	try {
        		System.out.print("\nEnter the number of nights you'll be staying: ");
        		input = inputSource.nextLine();
        		int nightsInput = Integer.parseInt(input);
        		
        		if(!Listing.isValidNightsRented(nightsInput)) {
        			throw new IllegalArgumentException();
        		}
        		else {
        			numberOfNights = nightsInput;
        			totalCost = Math.round((nightsInput * listing.getRentalPrice())*100.0)/100.0;
        			NightsFlag = false;
        		}
        	}
        	catch(Exception e) {
        		if(e instanceof NumberFormatException) {
        			System.out.print("\nInvalid Entry. Input must be numberic whole numbers.");
        		}
        		else if(e instanceof IllegalArgumentException) {
        			System.out.print("\nInput Entry. You can only rent a listing for 1 - 14 nights.");
        		}
        		else {
        			System.out.print("\nAn error occured, try again later.");
        		}
        		
        	}
        }
        
        //confirm Entry.
        while(confirmationFlag) {
        	
        	try {
        		System.out.print("\nListing ID: " + listing.getRentalUnitID() + "\nNights Rented: " +
        							numberOfNights + "\nCost Per Night: " + listing.getRentalPrice() +
        							"\nTotal Cost: " + totalCost + "\nEnter yes to confirm or no to cancel: ");
        		input = inputSource.nextLine();
        		if(input.equals("yes") || input.equals("Yes") || input.equals("YES") || input.equals("y") || input.equals("Y")) {
        			listing.setNightsRented(numberOfNights);
        			listing.setRentedFlag(true);
        			confirmationFlag = false;
        			System.out.print("\nTransaction Confirmed.\n");
        			//TODO: ADD LOG
        		}
        		else if(input.equals("no") || input.equals("No") || input.equals("NO") || input.equals("n") || input.equals("N")) {
        			confirmationFlag = false;
        			System.out.print("\nTransaction Cancelled.\n");
        		}
        		else {
        			throw new IllegalArgumentException();
        		}
        	}
        	catch(Exception e) {
        		if(e instanceof IllegalArgumentException) {
        			System.out.print("\nInvalid Entry. You must enter either yes or no to confirm.\n");
        		}
        		else {
        			System.out.print("\nAn error occured, try again.\n");
        		}
        	}
        }
        
    //Class logic goes here
        return activeUser;
    }
}
