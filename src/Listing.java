/**
 * @author KMDLLC
 *
 */
import java.util.UUID; 

public class Listing {
    private String _rentalUnitID;
    private final User _owner;
    private final String _city;
    private final double _rentalPrice;
    private final int _numberOfRooms;
    private boolean _rentedFlag;
    private int _nightsRented;
    
    //create new Listing
    /**
     * @param dataAccess
     * @param user
     * @param city
     * @param rentalPrice
     * @param numberOfRooms
     */
    public Listing(DataAccess dataAccess, User user, String city, double rentalPrice, int numberOfRooms) {
    	//will check if unique once dataAccess complete
    	boolean flag = true;
    	String generatedID;
    	while(flag) {
    		generatedID = generateUnitID();
    		if(!(dataAccess.listingExists(generatedID))) {
    			this._rentalUnitID = generatedID;
    			flag = false;
    		}
    	}
    	
    	this._owner = user;
    	this._city = city;
    	this._rentalPrice = rentalPrice;
    	this._numberOfRooms = numberOfRooms;
    	this._rentedFlag = false;
    	this._nightsRented = 0;
    }
    
    
    //instantiate listing objects
    /**
     * @param rentalUnitID
     * @param user
     * @param city
     * @param rentalPrice
     * @param numberOfRooms
     * @param rentedFlag
     * @param nightsRented
     */
    public Listing(String rentalUnitID, User user, String city, double rentalPrice, int numberOfRooms, boolean rentedFlag, int nightsRented) {
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
    public double getRentalPrice() {return _rentalPrice;}
    public int  getNumberOfRooms() {return _numberOfRooms;}
    public boolean isRented() {return _rentedFlag;}
    public int getNightsRented() {return _nightsRented;}
    
    public void setRentedFlag(boolean value) {
    	this._rentedFlag = value;
    }
    
    public void setNightsRented(int value) {
    	this._nightsRented = value;
    }
    
    //Checks if unitRentalID is 8 characters and alpha numeric using regex.
    /**
     * @param unitID
     * @return True
     * @return False
     */
    public static boolean isValidRentalID(String unitID) {
    	if((unitID.length() == 8) && (unitID.matches("^[a-zA-Z0-9]*$"))) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    // Checks if city is not between 1 to 25 characters
    // and if it's not alphabetic or contains -
    /**
     * @param city
     * @return false
     * @return true
     */
    public static boolean isValidCity(String city) {
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
    public static boolean isValidRentalPrice(double rentalPrice) {
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
    public static boolean isValidNumberofRooms(int numberOfRooms) {
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
    public static boolean isValidNightsRented(int nightsRented) {
    	if((nightsRented < 1) || (nightsRented > 14)) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
    
    //Creates a randomly Generated alphanumeric ID of 8 strings
    /**
     * @return generated ID string
     */
    private String generateUnitID() {
    	UUID randomUUID = UUID.randomUUID(); 
    	String generatedString = randomUUID.toString().replaceAll("-", "");
    	return generatedString.substring(0, 8);
    }
    
    // For testing purposes
    public String toString() {
    	return "Unit ID: " + _rentalUnitID + "\nUser: " + _owner.getUsername() +
    			"\nCity: " + _city + "\nPrice: " + String.valueOf(_rentalPrice) +
    			"\nRooms: " + String.valueOf(_numberOfRooms) + "\nRented: " + String.valueOf(_rentedFlag) + "\nNights: " + String.valueOf(_nightsRented);
    }
}
