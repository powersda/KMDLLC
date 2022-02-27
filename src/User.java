public class User {
    public enum UserType { ADMIN, FULL_STANDARD, RENT_STANDARD, POST_STANDARD }; //temp: this should exist in User
    
    private final String _username;
    private final UserType _userType;

    public String getUsername() { return _username; }
    public UserType getUserType() { return _userType; }

    public static UserType fromString(String shortUserType){
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

    public static boolean isValidUserType(String userType){
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
    
    public User(String username){ 
        //if (isValidUsername(username)){
            //System.out.println(isValidUsername(username));
            this._username = username;
        //}
        // else{
        //     System.out.println("Username Invalid.");
        // }
        this._userType = UserType.RENT_STANDARD; 
    }
    
    // Pass dataacess the addUser method
    // Returns false ... else returns true
    public static boolean isValidUsername(String username) { 

        //String validCharacters = "[A-Za-z]+";
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
