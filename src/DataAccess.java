import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class DataAccess {
    // private static DataAccess instance = new DataAccess();
    public DataAccess() {
        this("log/", "data/users.txt", "data/listings.txt");
    }

    public DataAccess(String transactionFile, String usersFile, String listingFile) {
        this._cachedUsers = new ArrayList<User>();
        this._cachedListings = new ArrayList<Listing>();
        this._sessionLogs = new ArrayList<Log>();
        this._newListings = new ArrayList<Listing>();

        this._usersLocation = usersFile;
        this._listingsLocation = listingFile;
        this._transactionFileLocation = transactionFile;
    }

    private final ArrayList<User> _cachedUsers;
    private final ArrayList<Listing> _cachedListings;
    private final ArrayList<Log> _sessionLogs;
    private final ArrayList<Listing> _newListings;
    private final String _usersLocation;
    private final String _listingsLocation;
    private final String _transactionFileLocation;

    // Singleton class: use this to get an instance
    // public static DataAccess getInstance() { return instance; }

    // Search for a cached user by username. Returns User object if found, null otherwise.
    public User getUser (String username) {
        for (User cachedUser : _cachedUsers){
            if (username.equals(cachedUser.getUsername()))
                return cachedUser;
        }
        // return null;
        return new User("David"); //DEBUG
    }

    // Search for a cached listing by rental unit ID. Returns listing object if found, null otherwise.
    public Listing getListing (String rentalUnitID) {
        for (Listing cachedListing : _cachedListings){
            if (rentalUnitID.equals(cachedListing.getRentalUnitID()))
                return cachedListing;
        }
        return null;
    }

    // Adds a user to the cached users list
    public void addUser (User newUser) { this._cachedUsers.add(newUser); }

    // Adds a listing to the cached listings list
    public void addListing (Listing newListing) { this._newListings.add(newListing); }

    // Adds a log to the current session's log queue
    public void addLog (Log newLog) { this._sessionLogs.add(newLog); }

    // Adds this session's newly created listings to the cached listing list
    public void commitNewListings () { this._cachedListings.addAll(this._newListings); }

    // Returns true if a listing exists in the cached listings list with the passed rental Unit ID, otherwise returns false
    public Boolean listingExists(String rentalUnitID) { return this.getListing(rentalUnitID) != null; }

    // Returns true if a user exists in the cached users list with the passed username, otherwise returns false
    public Boolean userExists(String username) { return this.getUser(username) != null; }

    // Returns true if the cached users list isn't empty (indicating the cache was already initialized), otherwise returns false
    public Boolean isLoaded() { return !_cachedUsers.isEmpty(); }

    // Reads the users.txt file and creates User objects from the contents, loading them into the cached users list
    public void loadUsers() throws FileNotFoundException{
        File users = new File(this._usersLocation);
        Scanner reader = new Scanner(users);
        while (reader.hasNextLine()){
            String data = reader.nextLine();
            //Create user object out of line and store in _cachedUsers
        }
        reader.close();
    }

    // Reads the listings.txt file and creates Listing objects from the contents, loading them into the cached listings list
    public void loadListings() throws FileNotFoundException {
        File listings = new File(this._listingsLocation);
        Scanner reader = new Scanner(listings);
        while (reader.hasNextLine()){
            String data = reader.nextLine();
            //Create listings object out of line and store in _cachedListings
        }
        reader.close();
    }


    // If a daily transaction file does not exist for today, creates it, otherwise opens it then write the contents of the session log to it, then clears the session log.
    public void writeDailyTransactionFile() {
    }
    



}
