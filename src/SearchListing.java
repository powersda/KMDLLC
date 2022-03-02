import java.util.Scanner;

public class SearchListing extends State {

    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {

        if (!Command.SEARCH.validateUser(activeUser))
                throw new SecurityException(activeUser == null? "You must be logged in before you can use this command!" : "Sorry, you don't have permission to use this command.");

        System.out.println("DEBUG: Running Search Listing...");
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
