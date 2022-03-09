/*******************************
* Class: CreateUser
* Description: Extends the State class to allow for the creation of new users
********************************/
import java.util.*;

public class CreateUser extends State {

	// Implements the execute method from State to create a user
    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {

        if (!Command.CREATE.validateUser(activeUser))
                throw new SecurityException(activeUser == null? "You must be logged in before you can use this command!" : "Sorry, you don't have permission to use this command");
        
    	String input;
       
        String username = null; 
        User.UserType userType = null;
    	boolean usernameFlag = true;
    	boolean userTypeFlag = true;

        // Input a new username 
        while(usernameFlag){
            try {
            	System.out.print("Enter new username: ");
           	 	input = inputSource.nextLine().trim();
           	 	

                if (!User.isValidUsername(input))
                    throw new IllegalArgumentException("Invalid username format.");
           	 	if (dbHandle.userExists(input))
                    throw new IllegalArgumentException("User already exists.");
            	username = input;
            	usernameFlag = false; // End of while loop
        	}
        	catch (IllegalArgumentException exception){
        		System.out.println(exception.getMessage());
        	}
        }
        
        // Input for the new users type
        while(userTypeFlag){
            try {
            	System.out.print("Enter users usertype " + Arrays.toString(User.UserType.values()) + ": ");
           	 	input = inputSource.nextLine().trim().toUpperCase();
           	 	
           	 	if (User.UserType.fromString(input) == null)
                    throw new IllegalArgumentException("Invalid user type.");
            	
                userType = User.UserType.fromString(input);
            	userTypeFlag = false; // End of while loop
        	}
        	catch (IllegalArgumentException exception){
        		System.out.println(exception.getMessage());
        	}
        }

        User newUser = new User(username, userType);

        // Creating the user
        try {
        	dbHandle.addUser(newUser);          
        	dbHandle.addLog(new Log(Log.TransactionCode.CREATE, newUser));
            System.out.println("User created!");
        }
        catch(Exception IOException) {
        	// Anticipate for any error, let user know this.
			System.out.println("An error occured, could not add user.");
        }
                        
        

        return activeUser;
    }
}
