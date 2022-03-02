public class User {
    public enum UserType { 
        ADMIN, FULL_STANDARD, RENT_STANDARD, POST_STANDARD;

        // Takes short-hand user type string and retuns UserType  

        public UserType fromString(String shortUserType){
            if (shortUserType == "AA")
                return UserType.ADMIN;
            else if (shortUserType == "FS")
                return UserType.FULL_STANDARD;
            else if (shortUserType == "BS")
                return UserType.RENT_STANDARD;
            else if (shortUserType == "SS")
                return UserType.POST_STANDARD;
            else
                return null;
        }

        // Checks if a string is a valid user type

        public boolean isValidUserType(String userType){
            if (userType == "ADMIN")
                return true;
            else if (userType == "FULL_STANDARD")
                return true;
            else if (userType == "RENT_STANDARD")
                return true;
            else if (userType == "POST_STANDARD")
                return true;
            else
                return false;
        }       
    }; //temp: this should exist in User
    
    private final String _username;
    private final UserType _userType;

    

    public String getUsername() { return _username; }
    public UserType getUserType() { return _userType; }

    // Checks if username is valid then sets it

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
        else if (username.length() > 15 | username.length() < 1){
            return false; 
        }
        else if (!username.matches("[A-Za-z]+")){
            return false; 
        }
        else{
            return true; 
        }
    }
}
