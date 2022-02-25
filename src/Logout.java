import java.util.Scanner;

public class Logout extends State {

    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {

        if (!Command.LOGOUT.validateUser(activeUser))
                throw new SecurityException(activeUser == null? "You must be logged in before you can use this command!" : "Sorry, you don't have permission to use this command.");

    
        // Write transaction file & clear logs
        dbHandle.commitNewListings();
        System.out.println("\nGoodbye!\n");
        State.showBanner();
        return null;
    }
}
