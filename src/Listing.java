/**
 * @author KMDLLC
 *
 */
public class Listing {
    private String _rentalUnitID;
    private final User _owner;
    private final String _city;
    private final Double _rentalPrice;
    private final Integer _numberOfRooms;
    private Boolean _rentedFlag;
    private Integer _nightsRented;
    
    
    public Listing(String rentalUnitID, User user, String city, Double rentalPrice, Integer numberOfRooms, Boolean rentedFlag, Integer nightsRented) {
    	this._rentalUnitID = rentalUnitID;
    	this._owner = user;
    	this._city = city;
    	this._rentalPrice = rentalPrice;
    	this._numberOfRooms = numberOfRooms;
    	this._rentedFlag = rentedFlag;
    	this._nightsRented = nightsRented;
    }
    
    public String getRentalUnitID() { return _rentalUnitID; }
    public User getOwner() {return _owner;}
    public String getCity() {return _city;}
    public Double getRentalPrice() {return _rentalPrice;}
    public Integer getNumberOfRooms() {return _numberOfRooms;}
    public Boolean isRented() {return _rentedFlag;}
    public Integer getNightsRented() {return _nightsRented;}
    
    //Checks if unitRentalID is 8 characters and alpha numeric using regex.
    /**
     * @param unitID
     * @return True
     * @return False
     */
    public static Boolean isValidRentalID(String unitID) {
    	if(unitID.length() != 8) {
    		return false;
    	}
    	else if (!unitID.matches("^[a-zA-Z0-9]*$")) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
    
    // Checks if city is not between 1 to 25 characters
    // and if it's not alphabetic or contains -
    /**
     * @param city
     * @return false
     * @return true
     */
    public static Boolean isValidCity(String city) {
    	if((city.length() < 1) || (city.length() > 25)) {
    		return false;
    	}
    	else if(!city.matches("^[a-zA-Z\u002D]*$")) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }

    // Checks if price is less than 1 or greater than 999.99
    // Doesn't check if it's numeric, because should not allow
    // non-double inputs anyways.
    /**
     * @param rentalPrice
     * @return true
     * @return false
     */
    public static Boolean isValidRentalPrice(Double rentalPrice) {
    	if((rentalPrice < 1) || (rentalPrice > 999.99)) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
    
    // Checks if rooms is less than 1 or greater than 9
    // Doesn't check if it's int, because should not allow
    // non-int inputs anyways.
    /**
     * @param numberOfRooms
     * @return true
     * @return false
     */
    public static Boolean isValidNumberofRooms(Integer numberOfRooms) {
    	if((numberOfRooms < 1) || (numberOfRooms > 9)) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
    
    // Checks if nights rented is less than 1 or greater than 
    // Doesn't check if it's int, because should not allow
    // non-int inputs anyways.
    /**
     * @param nightsRented
     * @return true
     * @return false
     */
    public static Boolean isValidNightsRented(Integer nightsRented) {
    	if((nightsRented < 1) || (nightsRented > 14)) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
    
    // For testing purposes
    public String toString() {
    	return "Unit ID: " + _rentalUnitID + "\nUser: " + _owner.toString() +
    			"\nCity: " + _city + "\nPrice: " + _rentalPrice.toString() +
    			"\nRooms: " + _numberOfRooms.toString() + "\nRented: " + _rentedFlag.toString() + "\nNights: " + _nightsRented.toString();
    }
}
