/*******************************
* Class: Login
* Description: Extends the State class to change the active User based on console input
********************************/

import java.io.IOException;
import java.lang.IllegalStateException;
import java.util.Scanner;

public class Login extends State {

    // Implements the execute method from State to set an active User based on console input
    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {

        if (!Command.LOGIN.validateUser(activeUser))
                throw new SecurityException("Please log out before using the \"" + State.Command.LOGIN + "\" command!");

        // Load users.txt
        if (!dbHandle.areUsersLoaded()) {
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
                String input = inputSource.nextLine().trim();
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

        // Load listings.txt
        if (!dbHandle.areListingsLoaded()) {
            try {
                dbHandle.loadListings();
            }
            catch (IOException | IllegalStateException exception) {
                System.out.println(exception.getMessage());
                return activeUser;
            }
        }

        System.out.println("Welcome " + activeUser.getUsername() + "!");
        return activeUser;
    }
}
