import java.util.Scanner;

public class CreateUser extends State {

    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {

        if (!Command.CREATE.validateUser(activeUser))
                throw new SecurityException(activeUser == null? "You must be logged in before you can use this command!" : "Sorry, you don't have permission to use this command");

        System.out.println("DEBUG: Running Create...");

    //Class logic goes here
        return activeUser;
    }
}