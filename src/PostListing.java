/*******************************
* Class: PostListing
* Description: Extends the State class to allow for users under specific usertypes to post their rental properties
********************************/
import java.util.Scanner;

public class PostListing extends State {
	// Implements the execute method from State to post a listing
    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {

        if (!Command.POST.validateUser(activeUser))
                throw new SecurityException(activeUser == null? "You must be logged in before you can use this command!" : "Sorry, you don't have permission to use this command.");
        
        String input;
        String city = null; 
        
        double rentalPrice = 0;
        int numberOfRooms = 0;

    	boolean cityFlag = true;
        boolean rentalPriceFlag = true;
    	boolean numberOfRoomsFlag = true;

        // Input for a city
        while(cityFlag){
            try {
            	System.out.print("\nEnter the city of the rental unit: ");
           	 	input = inputSource.nextLine();
           	 	
           	 	if (!Listing.isValidCity(input))
                    throw new IllegalArgumentException("Invalid city name.");
            	
            	city = input;
            	cityFlag = false; // End of while loop
        	}
        	catch (IllegalArgumentException exception){
        		System.out.println(exception.getMessage());
        	}
        }

        // Input for the rental price 
        while(rentalPriceFlag){
            try {
            	System.out.print("\nEnter the price per night for the unit: ");
           	 	input = inputSource.nextLine();
           	 	
           	 	try {
           	 	rentalPrice = Double.parseDouble(input);
           	 	}catch(Exception NumberFormatException) {
           	 		throw new IllegalArgumentException("Invalid price, must be numeric.");
           	 	}                    

           	 	if (!Listing.isValidRentalPrice(rentalPrice))
                    throw new IllegalArgumentException("Invalid price per night.");
            	
            	rentalPriceFlag = false; // End of while loop
        	}
        	catch (IllegalArgumentException exception){
        		System.out.println(exception.getMessage());
        	}
        }
        
        // Input for the numeber of rooms
        while(numberOfRoomsFlag){
            try {
            	System.out.print("\nEnter number of rooms in the unit: ");
           	 	input = inputSource.nextLine();
           	 	

                try {
                	numberOfRooms = Integer.parseInt(input);
           	 	}catch(Exception NumberFormatException) {
           	 		throw new IllegalArgumentException("Invalid number of rooms, must be numeric.");
           	 	}         
           	 	if (!Listing.isValidNumberofRooms(numberOfRooms))
                    throw new IllegalArgumentException("Invalid number of rooms.");
            	
            	numberOfRoomsFlag = false; // End of while loop
        	}
        	catch (IllegalArgumentException exception){
        		System.out.println(exception.getMessage());
        	}
        }
       
        // Creates the post
        try {
        	Listing newListing = new Listing(dbHandle, activeUser, city, rentalPrice, numberOfRooms);          
        	dbHandle.addListing(newListing);
        	dbHandle.addLog(new Log(Log.TransactionCode.POST, activeUser, newListing));
            System.out.println("\nListing created!");
        }
        catch(Exception e) {
        	// Anticipate for any error, let user know this.
			System.out.println("\nAn error occured, could not post listing.");
        }
        
        return activeUser;
    }
}
