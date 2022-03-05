/*******************************
* Class: Logout
* Description: Extends the State class to log out the current user, as well as trigger the write the daily transaction file
********************************/

import java.util.Scanner;
import java.io.IOException;

public class Logout extends State {

    // Implements the execute method from State to remove the current active user, as well as trigger the write to the daily transaction file 
    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {

        // Check to see if the user has permission for this state. If not, throw an exception.yy
        if (!Command.LOGOUT.validateUser(activeUser))
                throw new SecurityException(activeUser == null? "You must be logged in before you can use this command!" : "Sorry, you don't have permission to use this command.");

    
        // Log the end of session, then write to daily transaction file and add any newly created listings to the main listings cache
        dbHandle.addLog(new Log(Log.TransactionCode.END_OF_SESSION, activeUser));

        try {
            dbHandle.writeDailyTransactionFile();
            dbHandle.commitNewListings();
        }
        catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        System.out.println("\nGoodbye!\n");
        showBanner();
        return null;
    }
}
