/*
    The method from Listing being tested with line coverage:

    public String generateUnitID() {
    1.  String uid;
    2.  if (_isFirstListing) {
    3.      _isFirstListing = false;
    4.      uid = "AAAAAAAA";
        }
    5.  else
    6.        uid = (UUID.randomUUID().toString().replaceAll("-", "")).substring(0,8).toUpperCase();
    7.	return uid;
    }
    
    Lines 1, 2, and 7 are covered by both testFirstUnitID() and testNextUnitID():
    1. String uid;
    2. if (_isFirstListing)
    7. return uid;

    Lines 3 and 4 are covered by testFirstUnitID():
    3.  _isFirstListing = false;
    4.  uid = "AAAAAAAA";

    Lines 5 and 6 are covered by testNextUnitID():
    5. else
    6. uid = (UUID.randomUUID().toString().replaceAll("-", "")).substring(0,8).toUpperCase();



*/

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class generateUnitIDTest {
    Listing listingTest;
    // Creates first listing
    @BeforeEach
    @DisplayName("Test Initialization: Listing value reset")
    public void createNewInstance(){
       this.listingTest = null;
       
    }

    // Tests statement coverage for the generateUnitID.
    @Test
    @DisplayName("Line 1 \"true\" test, first unitID test") 
    // This is the first listing, the expected output should always be AAAAAAAA
    public void testFirstUnitID(){
        listingTest._isFirstListing = true;
        assertTrue(listingTest._isFirstListing, "This is the first listing");
        assertNull(listingTest, "First unitID is not null");
        listingTest = new Listing(CreateRandomUser(), CreateRandomCity(), CreateRandomPrice(), CreateRandomRoomNumber());
        assertFalse(listingTest._isFirstListing, "This is not the first listing");
        assertNotNull(listingTest.getRentalUnitID(), "First unitID was null");
        assertEquals(listingTest.getRentalUnitID().length(), 8, "UnitID does not equal 8 characters");
        assertTrue(listingTest.getRentalUnitID().equals("AAAAAAAA"), "First unitID was not AAAAAAAA");
        assertTrue(listingTest.getRentalUnitID().chars().allMatch(Character::isLetterOrDigit), "First unitID is non-alphanumeric");
    }


    @Test
    @DisplayName("Line 1 \"false\" test, next unitID test") 
    // The output should be a not null, 8 character, alphanumeric string.
    public void testNextUnitID(){
        listingTest._isFirstListing = false; // Making it so this is is not the first listing
        assertNull(listingTest, "UnitID should be null");
        assertFalse(listingTest._isFirstListing, "This is not the first listing");
        listingTest = new Listing(CreateRandomUser(), CreateRandomCity(), CreateRandomPrice(), CreateRandomRoomNumber());
        assertFalse(listingTest._isFirstListing, "This is still not the first listing");
        if (!listingTest._isFirstListing)
            assertFalse(listingTest.getRentalUnitID().equals("AAAAAAAA"), "Next unitID should not be AAAAAAAA (this has a 3.08E-31% chance of being a false negative, congratulations)");
        assertEquals(listingTest.getRentalUnitID().length(), 8, "Next unitID does not equal 8 characters");
        assertNotNull(listingTest.getRentalUnitID(), "Next unitID was null");
        assertTrue(listingTest.getRentalUnitID().chars().allMatch(Character::isLetterOrDigit), "Next unitID is non-alphanumeric");
    }


    // Used to create random valid user data for the listings test
    protected User CreateRandomUser() {
        Random rnd = new Random();
        List<User.UserType> userTypes = Arrays.asList(User.UserType.ADMIN, User.UserType.FULL_STANDARD, User.UserType.POST_STANDARD, User.UserType.RENT_STANDARD);
        User testUsers;
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder userID = new StringBuilder();
        while (userID.length() < 11) {
            int index = (int) (rnd.nextFloat() * CHARS.length());
            userID.append(CHARS.charAt(index));
        }
        String userIDStr = userID.toString();
        testUsers = new User(userIDStr, userTypes.get(rnd.nextInt(3))); 
        return testUsers;
    }

    // Used to create random valid city data for the listings test
    protected String CreateRandomCity() {
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
        StringBuilder cityName = new StringBuilder();
        Random rnd = new Random();
        while (cityName.length() < 16) {
            int index = (int) (rnd.nextFloat() * CHARS.length());
            cityName.append(CHARS.charAt(index));
        }
        String cityNameStr = cityName.toString();
        return cityNameStr;
    }

    // Used to create random valid price data for the listings test
    protected double CreateRandomPrice() {
        Random rnd = new Random();
        int numOfRooms = rnd.nextInt(99999);
        double randomPriceDouble = numOfRooms;
        randomPriceDouble = randomPriceDouble/100;
        return randomPriceDouble;
    }

    // Used to create random valid room number data for the listings test
    protected int CreateRandomRoomNumber() {
        Random rnd = new Random();
        int numOfRooms = rnd.nextInt(8);
        numOfRooms++;
        return numOfRooms;
    }
}
    
