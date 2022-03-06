/*******************************
* Class: DeleteUser
* Description: Extends the State class to allow for the deletion of users along with their posts
********************************/
import java.util.Scanner;



public class DeleteUser extends State {
	
	// Implements the execute method from State to delete a user and their posts
    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {

        if (!Command.DELETE.validateUser(activeUser))
                throw new SecurityException(activeUser == null? "You must be logged in before you can use this command!" : "Sorry, you don't have permission to use this command.");
        
        Listing[] listings;// = new ArrayList<Listing>();

        String username = null;
        boolean usernameFlag = true;
        
        User user;
        
        // Input for user to be deleted
        while(usernameFlag) {
            try {
                System.out.print("Please enter the user you would like to delete: ");
                String input = inputSource.nextLine().trim().toUpperCase();

                if (!User.isValidUsername(input))
                    throw new IllegalArgumentException("Invalid username format.");
                if (dbHandle.getUser(input).equals(activeUser.getUsername()))
                    throw new IllegalArgumentException("You cannot delete yourself.");
                if (!dbHandle.userExists(input))
                    throw new IllegalArgumentException("User does not exist.");
                
                listings = dbHandle.getListings(dbHandle.getUser(input));
                if (listings != null && listings.length != 0) {
                    for (Listing listing : listings){
                        if (input.equals(listing.getOwner().getUsername()))
                            if (listing.isRented())
                                throw new IllegalArgumentException("User has an open rental.");
                    }
                }
                //public Listing(String rentalUnitID, User user, String city, double rentalPrice, int numberOfRooms, boolean rentedFlag, int nightsRented) {
                
                username = input;
                usernameFlag = false;
            }
            catch (IllegalArgumentException exception){
                System.out.println(exception.getMessage());
            }
            
            // Deleting user and their posts
            try {
            	user = dbHandle.getUser(username);
            	dbHandle.removeUser(user);          
            	dbHandle.addLog(new Log(Log.TransactionCode.DELETE, user));
    			dbHandle.removeListings(dbHandle.getListings(user));	

                System.out.println("\nUser deleted.\n");
            }
            catch(Exception e) {
            	// Anticipate for any error, let user know this.
    			System.out.println("\nAn error occured, could not remove user.\n");
            }
        }
        return activeUser;
    }
}






