/*******************************
* Class: DeleteUser
* Description: Extends the State class to allow for the deletion of users along with their posts
********************************/
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class DeleteUser extends State {


    // Search if a user has any open rentals, else return false
    // public boolean userOpenRentals (String username, DataAccess dbHandle) {
    //     List<Listing> listings = new ArrayList<Listing>();
    //     listings = dbHandle.getListing (dbHandle.getUser(username));
    //     if (listings != null && !listings.isEmpty()) {
    //         for (Listing listing : listings){
    //             if (username.equals(listing.getOwner().getUsername()))
    //                 if (listing.isRented())
    //                     return true;
    //         }
    //     }
    //     return false;
    // }
    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {

        if (!Command.DELETE.validateUser(activeUser))
                throw new SecurityException(activeUser == null? "You must be logged in before you can use this command!" : "Sorry, you don't have permission to use this command.");
        
        Listing[] listings;// = new ArrayList<Listing>();
        String username = null;
        boolean usernameFlag = true;
        System.out.println("DEBUG: Running Delete...");
        while(usernameFlag) {
            try {
                System.out.print("Please enter the user you would like to delete: ");
                String input = inputSource.nextLine();
                if (!dbHandle.userExists(input))
                    throw new IllegalArgumentException("User does not exist.");
                if (!User.isValidUsername(input))
                    throw new IllegalArgumentException("Invalid username format.");
                if (dbHandle.getUser(input).equals(activeUser.getUsername()))
                    throw new IllegalArgumentException("You cannot delete yourself.");
                
                listings = dbHandle.getListings(dbHandle.getUser(input));
                if (listings != null && listings.length != 0) {
                    for (Listing listing : listings){
                        if (input.equals(listing.getOwner().getUsername()))
                            if (listing.isRented())
                                throw new IllegalArgumentException("User has an open rental.");
                    }
                }
                    
                username = input;
                usernameFlag = false;
            }
            catch (IllegalArgumentException exception){
                System.out.println(exception.getMessage());
            }
            
            try {
            	dbHandle.removeUser(dbHandle.getUser(username));          
                new Log(Log.TransactionCode.DELETE, dbHandle.getUser(username));
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






