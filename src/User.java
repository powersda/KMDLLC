/*******************************
* Class: User
* Description: Extends the State class for use with checking validity and setting usernames and usertypes
********************************/
public class User {
	
	// Enumeration that represents valid usertypes
    public enum UserType { 
        ADMIN("AA"), FULL_STANDARD("FS"), RENT_STANDARD("RS"), POST_STANDARD("PS");
    	// Returns UserType based on the string argument
        private UserType(String shortUserType) { this._userTypeString = shortUserType; }
        private String _userTypeString;
        // Returns a string based on the usertype
        public String toString() { return _userTypeString; }

        // Use this to list out enum values:
        // System.out.println(Arrays.toString(User.UserType.values()));
            
        // Takes short-hand user type string and returns UserType  
        public static UserType fromString(String input){
            for (UserType element : UserType.values()){
                if (element.toString().equals(input))
                    return element;
            }
            return null;
        }

    }; 
    
    private final String _username;
    private final UserType _userType;  

    public String getUsername() { return _username; }
    public UserType getUserType() { return _userType; }

    // Sets username and userType
    public User(String username, UserType userType){ 

        this._username = username;
        this._userType = userType;
    }
    
    // Checks if username string is valid, i.e. is alphabetic, within range, and not null

    public static boolean isValidUsername(String username) {
        if (username == null){
            return false; 
        }
        else if (username.length() > 15 || username.length() < 1){
            return false; 
        }
        else if (!username.matches("^[-a-zA-Z0-9_ ]*$")){
            return false; 
        }
        else{
            return true; 
        }
    }
}
