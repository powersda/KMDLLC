import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private final List<User> _cachedUsers;
    private final List<Listing> _cachedListings;
    private final List<Log> _sessionLogs;
    private final List<Listing> _newListings;
    private final String _usersLocation;
    private final String _listingsLocation;
    private final String _transactionFileLocation;

    // Singleton class: use this to get an instance
    // public static DataAccess getInstance() { return instance; }

    // Search for a cached user by username. Returns User object if found, null otherwise.
    public User getUser (String username) {
        if (_cachedUsers != null && !_cachedUsers.isEmpty() && username != null) {
            for (User cachedUser : _cachedUsers){
                if (username.equals(cachedUser.getUsername()))
                    return cachedUser;
            }
        }
        return null;
    }

    // Search for a cached listing by rental unit ID. Returns listing object if found, null otherwise.
    public Listing getListing (String rentalUnitID) {
        if (_cachedListings != null && !_cachedListings.isEmpty() && rentalUnitID != null) {
            for (Listing cachedListing : _cachedListings){
                if (rentalUnitID.equals(cachedListing.getRentalUnitID()))
                    return cachedListing;
            }
        }
        return null;
    }

    // Searches the cached listings for listings that match the past city, rental price, and number of rooms, and returns them in a listings array.
    // Parameters passed as "null" operate are equivalent to "any"
    public Listing[] searchListings (String city,  Double rentalPrice, Integer numberOfRooms) {
        List<Listing> searchResults = new ArrayList<Listing>();
        if (_cachedListings != null && !_cachedListings.isEmpty()) {
            for (Listing cachedListing : _cachedListings){
                if ((city == null? true : cachedListing.getCity().equals(city)) &&
                    (rentalPrice == null? true : cachedListing.getRentalPrice() == rentalPrice) &&
                    (numberOfRooms == null? true : cachedListing.getNumberOfRooms() == numberOfRooms))
                        searchResults.add(cachedListing);
            }
        }
        return searchResults.toArray(new Listing[searchResults.size()]);
    }

    // Adds a user to the cached users list
    public void addUser (User newUser) { 
        if (newUser != null && getUser(newUser.getUsername()) == null)
            this._cachedUsers.add(newUser);
    }

    // Adds a listing to the cached listings list
    public void addListing (Listing newListing) { 
        if (newListing != null && getListing(newListing.getRentalUnitID()) == null)
            this._newListings.add(newListing);
    }

    // Adds a log to the current session's log queue
    public void addLog (Log newLog) { this._sessionLogs.add(newLog); }

    public void removeUser(User user) {
        if (user != null)
            this._cachedUsers.remove(user);
    }

    // Adds this session's newly created listings to the cached listing list
    public void commitNewListings () { this._cachedListings.addAll(this._newListings); }

    // Returns true if a listing exists in the cached listings list with the passed rental Unit ID, otherwise returns false
    public boolean listingExists(String rentalUnitID) { return this.getListing(rentalUnitID) != null; }

    // Returns true if a user exists in the cached users list with the passed username, otherwise returns false
    public boolean userExists(String username) { return this.getUser(username) != null; }

    // Returns true if the cached users list isn't empty (indicating the cache was already initialized), otherwise returns false
    public boolean isUsersLoaded() { return !_cachedUsers.isEmpty(); }

    // Returns true if the cached listings list isn't empty (indicating the cache was already initialized), otherwise returns false
    public boolean isListingsLoaded() { return !_cachedListings.isEmpty(); }

    // Reads the users.txt file and creates User objects from the contents, loading them into the cached users list
    public void loadUsers() throws FileNotFoundException{
        File users = new File(this._usersLocation);
        Scanner reader = new Scanner(users);
        while (reader.hasNextLine()){
            String data = reader.nextLine();
             _cachedUsers.add(new User(data.substring(0, 15).trim(), User.UserType.fromString(data.substring(16, 18))));
        }
        reader.close();
    }

    // Reads the listings.txt file and creates Listing objects from the contents, loading them into the cached listings list
    // Requires users.txt be loaded already. Throws FileNotFoundException if listings.txt isn't found
    public void loadListings() throws FileNotFoundException {
        File listings = new File(this._listingsLocation);
        Scanner reader = new Scanner(listings);
        while (reader.hasNextLine()){
            String data = reader.nextLine();
            _cachedListings.add(new Listing( data.substring(0, 8), //Listing ID
                                             getUser(data.substring(9, 24).trim()), //User ID of owner
                                             data.substring(25, 50).trim(), // City
                                             Double.parseDouble(data.substring(53, 59)), // Price per Night
                                             Integer.parseInt(data.substring(51, 52).trim()), //Number of Rooms
                                             (data.substring(60, 61).equals("T") ? true : false), // rental flag
                                             Integer.parseInt(data.substring(62, 64)) // Number of nights remaining
            ));

        }
        reader.close();
    }


    // If a daily transaction file does not exist for today, creates it, otherwise opens it then write the contents of the session log to it, then clears the session log.
    public void writeDailyTransactionFile() {
        //TODO
    }
    



}
