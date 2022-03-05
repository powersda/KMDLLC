/*******************************
* Class: User
* Description: Extends the State class for use with checking validity and setting usernames and usertypes
********************************/
public class User {
    public enum UserType { 
        ADMIN("AA"), FULL_STANDARD("FS"), RENT_STANDARD("RS"), POST_STANDARD("PS");
        private UserType(String shortUserType) { this._userTypeString = shortUserType; }
        private String _userTypeString;
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

            // if (shortUserType.equals("AA"))
            //     return UserType.ADMIN;
            // else if (shortUserType.equals("FS"))
            //     return UserType.FULL_STANDARD;
            // else if (shortUserType.equals("RS"))
            //     return UserType.RENT_STANDARD;
            // else if (shortUserType.equals("PS"))
            //     return UserType.POST_STANDARD;
            // else
                // return null;
        }

        // Takes long form user type string and retuns UserType  

        // public static UserType fromStringUserType(String shortUserType){
        //     if (shortUserType.equals("ADMIN"))
        //         return UserType.ADMIN;
        //     else if (shortUserType.equals("FULL_STANDARD"))
        //         return UserType.FULL_STANDARD;
        //     else if (shortUserType.equals("RENT_STANDARD"))
        //         return UserType.RENT_STANDARD;
        //     else if (shortUserType.equals("POST_STANDARD"))
        //         return UserType.POST_STANDARD;
        //     else
        //         return null;
        // }

        // Checks if a string is a valid user type
        // public static boolean isValidUserType(String userType){

            // if (userType.equals("ADMIN"))
            //     return true;
            // else if (userType.equals("FULL_STANDARD"))
            //     return true;
            // else if (userType.equals("RENT_STANDARD"))
            //     return true;
            // else if (userType.equals("POST_STANDARD"))
            //     return true;
            // else
            //     return false;
        // }       
    }; 
    
    private final String _username;
    private final UserType _userType;

    

    public String getUsername() { return _username; }
    public UserType getUserType() { return _userType; }

    // Sets username and userType

    public User(String username, UserType userType){ 
        // try{
        //     if (!isValidUsername(username))
        //         throw new IllegalArgumentException("Username is not valid.");
        //     this._username = username;

        // }catch(IllegalArgumentException exception){
        //     System.out.println(exception.getMessage());
        // }

        this._username = username;
        this._userType = userType;
        //this._userType = UserType.RENT_STANDARD; 
    }
    
    // Pass dataacess the addUser method
    // Returns false ... else returns true

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
