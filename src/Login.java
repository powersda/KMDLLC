import java.io.FileNotFoundException;
import java.util.Scanner;

public class Login extends State {

    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {

        if (!Command.LOGIN.validateUser(activeUser))
                throw new SecurityException("Please log out before using the \"" + State.Command.LOGIN + "\" command!");

        // Load users.txt db
        if (!dbHandle.isLoaded()) {
            try {
                dbHandle.loadUsers();
            }
            catch (FileNotFoundException exception) {
                System.out.println("Unable to load users.txt file!");
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

        if (!dbHandle.isLoaded()) {
            try {
                dbHandle.loadListings();
            }
            catch (FileNotFoundException exception) {
                System.out.println("Unable to load listings.txt file!");
                return activeUser;
            }
        }

        // activeUser = dbHandle.getUser(input);
        System.out.println("\nWelcome " + activeUser.getUsername() + "!\n");
        return activeUser;
    }
}
