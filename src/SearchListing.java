import java.util.Scanner;

public class SearchListing extends State {

    /**
     *
     */
    /**
     *
     */
    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {
    	String city = null; 
    	String input;
        Double rentalPrice = 0;
        Integer numberOfRooms = 0;
    	// flags for input checking loops
    	boolean cityFlag = true;
    	boolean rentalPriceFlag = true;
    	boolean roomsFlag = true;
    	Listing[] listings; 
    			
        if (!Command.SEARCH.validateUser(activeUser))
                throw new SecurityException(activeUser == null? "You must be logged in before you can use this command!" : "Sorry, you don't have permission to use this command.");
        
        
        //City input and validation
        while(cityFlag) {
        	try {
            	System.out.print("\nEnter the city of the listing: ");
           	 	input = inputSource.nextLine();
           	 	
           	 	//Checks for wild card
           	 	if(input.equals("*")) {
           	 		input = null;
           	 	}
           	 	else if(!Listing.isValidCity(input)) {
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
        
        
        //rental price input and validation
        while(rentalPriceFlag) {
        	try {
            	System.out.print("\nNote: Prices will be rounded to two decimal places. \nEnter the rental price: ");
            	input = inputSource.nextLine();
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
        			//refer line 64
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
        
      //rental price input and validation
        while(roomsFlag) {
        	try {
            	System.out.print("\nEnter the number of bedrooms: ");
            	input = inputSource.nextLine();
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
        			// refer to line 99
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
        
        //print results
        try {
        	listings = dbHandle.searchListings(city, rentalPrice, numberOfRooms);
        	//Check if searchlistings couldn't find anything.
            if(listings.length == 0) {
            	System.out.println("No Results Found."); 
            }
            else {
            	//print all listings.
            	for (Listing element: listings) {
                    System.out.println(element.toString());
            	}
            }
        }
        catch(Exception e) {
        	// Anticipate for any error, let user know this.
			System.out.println("\nAn error occured, could not search listings.");
        }
        
        // what's this??
        // while(true) {
        //     try {
        //         System.out.print("Search: ");
        //         String input = inputSource.nextLine();
        //         if (!dbHandle.userExists(input))
        //             throw new IllegalArgumentException("User does not exist.");
        //         if (!User.isValidUsername(input))
        //             throw new IllegalArgumentException("Invalid username format.");
        //         if (dbHandle.getUser(input).equals(activeUser.getUsername()))
        //             throw new IllegalArgumentException("You cannot delete yourself.");
                
        //     }
        //     catch (IllegalArgumentException exception){
        //         System.out.println(exception.getMessage());
        //     }
        // }
    
        return activeUser;
    }
}
