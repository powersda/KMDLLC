/*******************************
* Class: Log 
* Description: Represents a log to be written to the daily transaction file.
********************************/

public class Log {

    // Enumeration that represents a valid Transaction Code
    public enum TransactionCode {
        END_OF_SESSION("00"), CREATE("01"), DELETE("02"), POST("03"), SEARCH("04"), RENT("05") ;

        private String _code;

        // Constructor: creates the enumeration with an appropriate string representation
        private TransactionCode(String code){ this._code = code; }

        // Returns the Transaction Code's string representation
        public String toString() { return _code; }
    }

    private final TransactionCode _transactionCode;
    private final String _username;
    private final User.UserType _userType;
    private final String _rentalUnitID;
    private final String _city;
    private final int _numberOfRooms;
    private final double _rentalPrice;
    private final int _nightsRented;

    // Constructor: creates a Log without an associated Listing
    public Log(TransactionCode code, User user) {
       this(code, user, null);
    }

    // Constructor: creates a Log from passed Transaction Code, User, and Listing
    public Log(TransactionCode code, User user, Listing listing) {
        if (code == null || user == null)
            throw new IllegalArgumentException();

        this._transactionCode = code;
        this._username = user.getUsername();
        this._userType = user.getUserType();

        if (listing != null) {
            this._rentalUnitID = listing.getRentalUnitID();
            this._city = listing.getCity();
            this._numberOfRooms = listing.getNumberOfRooms();
            this._rentalPrice = listing.getRentalPrice();
            this._nightsRented = listing.getNightsRented();
        }
        else {
            this._rentalUnitID = "";
            this._city = "";
            this._numberOfRooms = 0;
            this._rentalPrice = 0;
            this._nightsRented = 0;
        }
    }

    // Returns the Log's Transaction Code
    public TransactionCode getTransactionCode() { return _transactionCode; }

    // Returns the Log's associated username
    public String getUsername() { return _username; }

    // Returns the Log's associated User Type
    public User.UserType getUserType() { return _userType; }

    // Returns the Log's associated rental unit ID
    public String getRentalUnitID() { return _rentalUnitID; }

    // Returns the Log's associated city
    public String getCity() { return _city; }

    // Returns the Log's associated number of rooms
    public int getNumberOfRooms() { return _numberOfRooms; }

    // Returns the Log's associated rental price
    public double getRentalPrice() { return _rentalPrice; }

    // Returns the Log's associated number of nights 
    public int getNightsRented() { return _nightsRented; }
}
    
