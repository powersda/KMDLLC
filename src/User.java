public class User {
    public enum UserType { ADMIN, FULL_STANDARD, RENT_STANDARD, POST_STANDARD }; //temp: this should exist in User

    private final String _username;
    private final UserType _userType;

    public String getUsername() { return _username; }
    public UserType getUserType() { return _userType; }
    public User(String username){ this._username = username; this._userType = UserType.RENT_STANDARD; }
    public static Boolean isValidUsername(String username) { return true; }
}
