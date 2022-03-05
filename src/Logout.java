import java.util.Scanner;
import java.io.IOException;

public class Logout extends State {

    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {

        if (!Command.LOGOUT.validateUser(activeUser))
                throw new SecurityException(activeUser == null? "You must be logged in before you can use this command!" : "Sorry, you don't have permission to use this command.");

    
        // Write transaction file & clear logs
        dbHandle.addLog(new Log(Log.TransactionCode.END_OF_SESSION, activeUser));

        try {
            dbHandle.writeDailyTransactionFile();
            dbHandle.commitNewListings();
        }
        catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        System.out.println("\nGoodbye!\n");
        State.showBanner();
        return null;
    }
}
