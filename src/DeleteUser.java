import java.util.Scanner;

public class DeleteUser extends State {

    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {

        if (!Command.DELETE.validateUser(activeUser))
                throw new SecurityException(activeUser == null? "You must be logged in before you can use this command!" : "Sorry, you don't have permission to use this command.");


        System.out.println("DEBUG: Running Delete...");
        while(true) {
            try {
                System.out.print("Please enter the user you would like to delete: ");
                String input = inputSource.nextLine();
                if (!dbHandle.userExists(input))
                    throw new IllegalArgumentException("User does not exist.");
                if (!User.isValidUsername(input))
                    throw new IllegalArgumentException("Invalid username format.");
                if (dbHandle.getUser(input).equals(activeUser.getUsername()))
                    throw new IllegalArgumentException("You cannot delete yourself.");
                
            }
            catch (IllegalArgumentException exception){
                System.out.println(exception.getMessage());
            }
        }
        //return activeUser;
    }
}
