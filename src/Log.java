public class Log {
    public enum TransactionCode {
        END_OF_SESSION("00"), CREATE("01"), DELETE("02"), POST("03"), SEARCH("04"), RENT("05") ;

        private String _code;
        private TransactionCode(String code){ this._code = code; }
        public String toString() { return _code; }
    }

    //To do: object deep copy
    private final String _transactionString;

    public Log(TransactionCode code, User user) {
       this(code, user, null);
    }

    public Log(TransactionCode code, User user, Listing listing) {
        this._transactionString = "TEMP";
    }
    
    public String toString() { return _transactionString; }
}
