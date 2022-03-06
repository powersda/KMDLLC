/*******************************
* Class: SearchListing
* Description: Handles Search Listing Transactions and validates inputs.
* 			   Output should display all found listings where rented flag = false or no results found. Once completed it will log transaction
********************************/
import java.util.Scanner;

// Extends the State class to allow for users that are active to Search Listings and validate user input.
public class SearchListing extends State {

	// TODO: TEST and Maybe implement temp listing to record user search input for non existent or existent listing?
	
	// Implements the execute method from State to present the Search listings prompts
    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {
    	// Check user type before declaring variables.
        if (!Command.SEARCH.validateUser(activeUser))
                throw new SecurityException(activeUser == null? "You must be logged in before you can use this command!" : "Sorry, you don't have permission to use this command.");
        
        // Default variables set to null to prevent "variable not initialized" error in loops.
    	String city = null; 
    	String input;
        Double rentalPrice = null;
        Integer numberOfRooms = null;
    	// flags for input checking loops
    	boolean cityFlag = true;
    	boolean rentalPriceFlag = true;
    	boolean roomsFlag = true;
    	Listing[] listings; 
    	
        // Validates City input and makes sure the city input is Alphabetic contains "-", 1-25 character inclusive
    	// Using Listing class isValidCity() method or allows for user to enter * as a wild card to return all listings.
    	// Does not allow User to move on if input is not correctly entered.
        while(cityFlag) {
        	try {
            	System.out.print("\nEnter the city of the listing: ");
           	 	input = inputSource.nextLine().trim();
           	 	
           	 	//Checks for wild card
           	 	if(input.equals("*")) {
           	 		input = null;
           	 	}
           	 	else if(!Listing.isValidCity(input)) {
           	 		// Input was not Alphabetic or contained "-" or 1-25 characters nor was it a *.
            		throw new IllegalArgumentException();
            	}
            	
            	city = input;
            	cityFlag = false; //end while loop
        	}
        	catch (Exception e){
        		if(e instanceof IllegalArgumentException) {
        			System.out.println("\nInvalid city format.");
        		}
        		else {
        			// Anticipate for any error, let user know this.
        			System.out.println("\nAn error occured, try again.");
        		}
        	}	
        }
        
        
        // Validates rental Price input and makes sure the Rental Price input is numeric and between 1 - 999.99 inclusive.
        // Also rounds input incase user enters over 2 decimal places as input. Or allows single * input as a wild card.
        // Does not allow User to move on if input is not correctly entered.
        while(rentalPriceFlag) {
        	try {
            	System.out.print("\nNote: Prices will be rounded to two decimal places. \nEnter the maximum rental price per night: ");
            	input = inputSource.nextLine().trim();
            	//Checks for wild card
        		if(input.equals("*")) {
        			rentalPrice = null;
        		}
        		else {
        			// since it's not  a wild card it should not accept anything else than a double
        			// Double parse checks for non numeric input for us, thanks parse! :)
               	 	double priceInput = Double.parseDouble(input);
                	if(!Listing.isValidRentalPrice(priceInput)) {
                		throw new IllegalArgumentException();
                	}
                	rentalPrice = Math.round(priceInput*100.0)/100.0; //round to two decimal places.
        		}
            	rentalPriceFlag = false; //end while loop
            }
        	catch (Exception e){
        		if(e instanceof NumberFormatException) {
        			//refer line 74
        			System.out.println("\nInvalid Entry. Input must be numberic or try entering \"*\" for all prices");
        		}
        		else if(e instanceof IllegalArgumentException) {
        			System.out.println("\nInvalid Entry. Price must be between 1 - 999.99.");
        		}
        		else {
        			// Anticipate for any error, let user know this.
        			System.out.println("\nAn error occured, try again.");
        		}
        	}	
        }
        
        // Validates rental rooms input and makes sure that input is a numeric integer and input between 1 - 9.
        // or * as a wild card.
        // Does not allow User to move on if input is not correctly entered.
        while(roomsFlag) {
        	try {
            	System.out.print("\nEnter the minimum number of bedrooms: ");
            	input = inputSource.nextLine().trim();
            	//Checks for wild card
        		if(input.equals("*")) {
        			numberOfRooms = null;
        		}
        		else {
        			// since it's not  a wild card it should not accept anything else than an int
        			// Integer parse checks for non numeric input for us, thanks parse! :)
               	 	int roomsInput = Integer.parseInt(input);
                	if(!Listing.isValidNumberofRooms(roomsInput)) {
                		throw new IllegalArgumentException();
                	}
                	numberOfRooms = roomsInput;
        		}
        		roomsFlag = false;
            }
        	catch (Exception e){
        		if(e instanceof NumberFormatException) {
        			// refer to line 111
        			System.out.println("\nInvalid Entry. Input must be numberic whole numbers or try entering \"*\" for all number of rooms.");
        		}
        		else if(e instanceof IllegalArgumentException) {
        			System.out.println("\nInvalid Entry. Number of Rooms must be between 1 - 9.");
        		}
        		else {
        			// Anticipate for any error, let user know this.
        			System.out.println("\nAn error occured, try again.");
        		}
        	}	
        }
        
        // Tries to print the results based of user inputs. Using input variables line 137 calls db Handle
        // to search for listings if not found array should be of length 0 and no results prompt should be displayed
        // if found it will print all listings returned that are not rented.
        // If there is an error with the db Handle call, it will be catched and displayed as an user friendly error
        // without crashing the program.
        try {
        	listings = dbHandle.searchListings(city, rentalPrice, numberOfRooms);
        	//Listing tempListing = new Listing("        ",activeUser,city,rentalPrice,numberOfRooms,false,0);
        	
        	dbHandle.addLog(new Log(Log.TransactionCode.SEARCH, activeUser)); /// add new log.
        	//tempListing = null;
        	//Check if search listings couldn't find anything.
            if(listings.length == 0) {
            	System.out.println("No Results Found."); 
            }
            else {
            	//print all listings.
            	for (Listing element: listings) {
            		if(!element.isRented()) {
            			System.out.println("---------------\n" + element.toStringSearch() + "\n");
            		} 
            	}
            }
        }
        catch(Exception e) {
        	// Anticipate for any error, let user know this.
			System.out.println("\nAn error occured, could not search listings.");
        }
        
    
        return activeUser;
    }
}
