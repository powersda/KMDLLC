import java.util.Scanner;

public class CreateUser extends State {

    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException {

        if (!Command.CREATE.validateUser(activeUser))
                throw new SecurityException(activeUser == null? "You must be logged in before you can use this command!" : "Sorry, you don't have permission to use this command");
        
    	String input;

        String username = null; 
        User.UserType userType = null;
    	boolean usernameFlag = true;
    	boolean userTypeFlag = true;
        System.out.println("DEBUG: Running Create...");

        // Input a new username 
        while(usernameFlag){
            try {
            	System.out.print("\nEnter new username: ");
           	 	input = inputSource.nextLine();
           	 	
           	 	if (dbHandle.userExists(input))
                    throw new IllegalArgumentException("User already exists.");
                if (!User.isValidUsername(input))
                    throw new IllegalArgumentException("Invalid username format.");
            	
            	username = input;
            	usernameFlag = false; // End of while loop
        	}
        	catch (IllegalArgumentException exception){
        		System.out.println(exception.getMessage());
        	}
        }

        while(userTypeFlag){
            try {
            	System.out.print("\nEnter new username: ");
           	 	input = inputSource.nextLine();
           	 	
           	 	if (!User.UserType.isValidUserType(input))
                    throw new IllegalArgumentException("Invalid user type.");
            	
                userType = User.UserType.fromStringUserType(input);
            	userTypeFlag = false; // End of while loop
        	}
        	catch (IllegalArgumentException exception){
        		System.out.println(exception.getMessage());
        	}
        }


        // try {
        // 	dbHandle.addUser(username);
        // }
        // catch(Exception e) {
        // 	// Anticipate for any error, let user know this.
		// 	System.out.println("\nAn error occured, could not search listings.");
        // }
                        


    //Class logic goes here
        return activeUser;
    }
}
