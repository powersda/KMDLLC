/*
    The method from DataAccess being tested with decision and loop coverage, which contains two decisions and one loop:

    public User getUser (String username) {
        if (!_cachedUsers.isEmpty() && username != null) {
            for (User cachedUser : _cachedUsers){
                if (username.equals(cachedUser.getUsername()))
                    return cachedUser;
            }
        }
        return null;
    } 

    The first decision's "false" branch is tested in noUsersLoadedTest()
    The first decision's "true" branch is implicitly tested in all future tests
    The loop's 0-iteration test case is not possible, since the previous condition statement precludes this state
    The loop's 1-iteration test case is tested in iterateUsersLoopTest() with a parameter of 1 (indicating 1 User in the cache)
    The loop's 2-iteration test case is tested in iterateUsersLoopTest() with a parameter of 2 (indicating 2 Users in the cache)
    The loop's many-iteration test case is tested in iterateUsersLoopTest() with a parameter of 100 (indicating 100 Users in the cache)
    The second decision's "true" branch is implicitly tested in the above three loop tests
    The second decision's "false" branch is implicitly tested in iterateUsersLoopTwice() and iterateUsersLoopMany()
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
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @DisplayName("Decision 1 \"true\" test, first unitID test") 
    @Order(1)
    // This is the first listing, the expected output should always be AAAAAAAA
    public void testFirstUnitID(){
        assertTrue(listingTest._isFirstListing, "This is the first listing");
        assertNull(listingTest, "First unitID is not null");
        listingTest = new Listing(CreateRandomUser(), CreateRandomCity(), CreateRandomPrice(), CreateRandomRoomNumber());
        assertFalse(listingTest._isFirstListing, "This is not the first listing");
        assertEquals(listingTest.getRentalUnitID().length(), 8, "UnitID does not equal 8 characters");
        assertTrue(listingTest.getRentalUnitID().equals("AAAAAAAA"), "First unitID was not AAAAAAAA");
        assertNotNull(listingTest.getRentalUnitID(), "First unitID was null");
        assertTrue(listingTest.getRentalUnitID().chars().allMatch(Character::isLetterOrDigit), "First unitID is non-alphanumeric");
    }


    @Test
    @DisplayName("Decision 2 \"true\" test, next unitID test") 
    @Order(2)
    // The output should be a not null, 8 character, alphanumeric string.
    public void testNextUnitID(){
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
    
