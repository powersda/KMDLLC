public class Log {
    public enum TransactionCode {
        END_OF_SESSION("00"), CREATE("01"), DELETE("02"), POST("03"), SEARCH("04"), RENT("05") ;

        private String _code;
        private TransactionCode(String code){ this._code = code; }
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

    public Log(TransactionCode code, User user) {
       this(code, user, null);
    }

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

    public TransactionCode getTransactionCode() { return _transactionCode; }
    public String getUsername() { return _username; }
    public User.UserType getUserType() { return _userType; }
    public String getRentalUnitID() { return _rentalUnitID; }
    public String getCity() { return _city; }
    public int getNumberOfRooms() { return _numberOfRooms; }
    public double getRentalPrice() { return _rentalPrice; }
    public int getNightsRented() { return _nightsRented; }
}
    
