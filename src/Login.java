import java.io.IOException;
import java.util.Scanner;

public class Login extends State {

    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {

        if (!Command.LOGIN.validateUser(activeUser))
                throw new SecurityException("Please log out before using the \"" + State.Command.LOGIN + "\" command!");

        // Load users.txt db
        if (!dbHandle.isUsersLoaded()) {
            try {
                dbHandle.loadUsers();
            }
            catch (IOException exception) {
                System.out.println(exception.getMessage());
                return activeUser;
            }
        }

        // Username validation
        while(activeUser == null) {
            try {
                System.out.print("Please enter your username: ");
                String input = inputSource.nextLine();
                if (!User.isValidUsername(input))
                    throw new IllegalArgumentException("Invalid username format.");
                if (!dbHandle.userExists(input))
                    throw new IllegalArgumentException("Username not found.");

                activeUser = dbHandle.getUser(input);
            }
            catch (IllegalArgumentException exception){
                System.out.println(exception.getMessage());
            }
        }

        if (!dbHandle.isListingsLoaded()) {
            try {
                dbHandle.loadListings();
            }
            catch (IOException exception) {
                System.out.println(exception.getMessage());
                return activeUser;
            }
        }

        // activeUser = dbHandle.getUser(input);
        System.out.println("\nWelcome " + activeUser.getUsername() + "!\n");
        return activeUser;
    }
}
