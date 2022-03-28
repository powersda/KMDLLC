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
    The loop's 1-iteration test case is tested in iterateUsersLoopOnce()
    The loop's 2-iteration test case is tested in iterateUsersLoopTwice()
    The loop's many-iteration test case is tested in iterateUsersLoopMany()
    The second decision's "true" branch is implictly tested in the above three loop tests
    The second decision's "true" branch is implictly tested in iterateUsersLoopTwice() and iterateUsersLoopMany()
*/

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class getUserTest {

    @Test
    public void noUsersLoadedTest() {
        // Create the DataAccess object, but dont load it
        DataAccess testDataAccess = new DataAccess(null, "junitInputs/oneUser.txt", null);
        assertNotNull(testDataAccess, "Failure to instantiate a DataAccess object");

        // Try to find a user
        User foundUser = testDataAccess.getUser(null);
        assertNull(foundUser, "foundUser was NOT null before loading users.txt");
    }

    @Test
    public void iterateUsersLoopOnce() {
        // Create the DataAccess object
        DataAccess testDataAccess = new DataAccess(null, "junitInputs/oneUser.txt", null);
        assertNotNull(testDataAccess, "Failure to instantiate a DataAccess object");

        // Load the users.txt file. NOTE: this custom users.txt file only has ONE entry, meaning there will only be 1 loop iteration
        try {
            testDataAccess.loadUsers();
        }
        catch (IOException e) {}
        assertTrue(testDataAccess.areUsersLoaded(), () -> "Failure to load users from users.txt");

        // Try to find an existing user (can take 1 iteration, since only one user was loaded);
        String existingUsername = "User001";
        User foundUser = testDataAccess.getUser(existingUsername);
        assertNotNull(foundUser, "foundUser was null after loading users.txt");
        assertTrue(foundUser.getUsername().equals(existingUsername), () -> "The returned User's username did match the username that was searched for");
    }

    @Test
    public void iterateUsersLoopTwice() {
        // Create the DataAccess object
        DataAccess testDataAccess = new DataAccess(null, "junitInputs/twoUsers.txt", null);
        assertNotNull(testDataAccess, "Failure to instantiate a DataAccess object");

        // Load the users.txt file. NOTE: this custom users.txt file only has ONE entry, meaning there will only be 1 loop iteration
        try {
            testDataAccess.loadUsers();
        }
        catch (IOException e) {}
        assertTrue(testDataAccess.areUsersLoaded(), () -> "Failure to load users from users.txt");

        // Try to find an existing user (can take 1 iteration, since only one user was loaded);
        String existingUsername = "User002";
        User foundUser = testDataAccess.getUser(existingUsername);
        assertNotNull(foundUser, "foundUser was null after loading users.txt");
        assertTrue(foundUser.getUsername().equals(existingUsername), () -> "The returned User's username did match the username that was searched for");
    }

    @Test
    public void iterateUsersLoopMany() {
        // Create the DataAccess object
        DataAccess testDataAccess = new DataAccess(null, "junitInputs/manyUsers.txt", null);
        assertNotNull(testDataAccess, "Failure to instantiate a DataAccess object");

        // Load the users.txt file. NOTE: this custom users.txt file only has ONE entry, meaning there will only be 1 loop iteration
        try {
            testDataAccess.loadUsers();
        }
        catch (IOException e) {}
        assertTrue(testDataAccess.areUsersLoaded(), () -> "Failure to load users from users.txt");

        // Try to find an existing user (can take 1 iteration, since only one user was loaded);
        String existingUsername = "User010";
        User foundUser = testDataAccess.getUser(existingUsername);
        assertNotNull(foundUser, "foundUser was null after loading users.txt");
        assertTrue(foundUser.getUsername().equals(existingUsername), () -> "The returned User's username did match the username that was searched for");
    }
}
