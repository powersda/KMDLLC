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
    The second decision's "true" branch is implictly tested in the above three loop tests
    The second decision's "true" branch is implictly tested in iterateUsersLoopTwice() and iterateUsersLoopMany()
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
import java.io.IOException;

public class getUserTest extends DataAccess {

    // Resets the user cache before each test
    @BeforeEach
    @DisplayName("Test Initialization: User cache reset")
    public void clearCacheContents() {
        this._cachedUsers.clear();
        this._usersAreLoaded = false;
    }

    // Test that the method always returns null when the users cache is empty
    @Test
    @DisplayName("Decision 1 \"false\" test")
    public void noUsersLoadedTest() {

        // Make sure that the cache is empty
        assertNotNull(this._cachedUsers, "Cached user list was null");
        assertTrue(this._cachedUsers.isEmpty(), "Cached users ArrayList was not empty");

        // Try to find a user
        User foundUser = this.getUser("User001");
        assertNull(foundUser, "foundUser was NOT null before loading users.txt");
    }

    // Test looping through the user cache to find a specific user. Uses parameterized inputs to test various amounts of loop iteration
    @ParameterizedTest
    @DisplayName("Decision 1 \"true\" test, Loop 1 loop coverage test, Decision 2 \"true\" and \"false\" test") 
    @ValueSource(ints = { 1, 2, 100 })
    public void iterateUsersLoopTest(int numberOfUsers) {

        // Make sure that the cache is empty
        assertTrue(this._cachedUsers.isEmpty(), "User cache was not empty before adding User object(s)");

        // Generate User objects to be inserted into cache
        List<User.UserType> userTypes = Arrays.asList(User.UserType.ADMIN, User.UserType.FULL_STANDARD, User.UserType.POST_STANDARD, User.UserType.RENT_STANDARD);
        String testUserPrefix = "testUser";
        @SuppressWarnings("unchecked") // All objects being added to the List are homogenous, so we can ignore this warning
        List<User> testUsers = new ArrayList();
        for (int i = 0; i < numberOfUsers; ++i) 
            testUsers.add(new User(testUserPrefix + i, userTypes.get(i % userTypes.size()))); 

        // Add User objects to user cache
        for (User user : testUsers)
            this._cachedUsers.add(user);

        // Make sure the user cache is the expected size after adding User object(s)
        assertFalse(this._cachedUsers.isEmpty(), "User cache was empty after adding User object(s)");
        assertEquals(this._cachedUsers.size(), testUsers.size(), "User cache was not the expected size after adding User object(s)");

        // Make sure the user cache contents are of the expected values and in the expected order
        for (int i = 0; i < this._cachedUsers.size(); ++i) {
            assertTrue(this._cachedUsers.get(i).getUsername().equals(testUsers.get(i).getUsername()), "User object added to cache did not have the expected username");
            assertTrue(this._cachedUsers.get(i).getUserType().equals(testUsers.get(i).getUserType()), "User object added to cache did not have the expected user type");
        }

        // Search for the last user inserted into the user cache, forcing it to iterate a controlled number of times
        String targetUsername = testUserPrefix + (numberOfUsers - 1);
        User foundUser = this.getUser(targetUsername);

        // Verify the User object that was returned
        assertNotNull(foundUser, "getUser returned null");
        assertTrue(foundUser.getUsername().equals(targetUsername), () -> "The returned User object's username did match the username that was searched for");
        assertTrue(foundUser.getUserType().equals(userTypes.get((numberOfUsers - 1) % userTypes.size())), () -> "The returned User object's user type did not match the user type originally added to the cache");
    }
}
