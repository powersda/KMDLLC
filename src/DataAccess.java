/*******************************
* Class: DataAccess
* Description: Handles the program's data by abstracting input and output functionality, providing an interface to access and modify data as needed
********************************/

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;
import java.lang.IllegalStateException;

public class DataAccess {
    protected final List<User> _cachedUsers;
    protected final List<Listing> _cachedListings;
    protected final List<Log> _sessionLogs;
    protected final List<Listing> _newListings;
    protected final String _usersLocation;
    protected final String _listingsLocation;
    protected final String _transactionFileLocation;
    protected boolean _listingsAreLoaded;
    protected boolean _usersAreLoaded;

    // Default constructor for DataAccess that uses default input and output file locations
    public DataAccess() {
        this("log/", "data/users.txt", "data/listings.txt");
    }

    // Parameterized constructor for DataAccess that takes custom input and output file locations
    public DataAccess(String transactionFile, String usersFile, String listingFile) {
        this._cachedUsers = new ArrayList<User>();
        this._cachedListings = new ArrayList<Listing>();
        this._sessionLogs = new ArrayList<Log>();
        this._newListings = new ArrayList<Listing>();
        _listingsAreLoaded = false;
        _usersAreLoaded = false;

        this._usersLocation = usersFile;
        this._listingsLocation = listingFile;
        this._transactionFileLocation = transactionFile;
    }

    // Searches for a cached User by username. Returns User if found, null otherwise.
    public User getUser (String username) {
        if (!_cachedUsers.isEmpty() && username != null) {
            for (User cachedUser : _cachedUsers){
                if (username.equals(cachedUser.getUsername()))
                    return cachedUser;
            }
        }
        return null;
    }

    // Searches for a cached Listing by rental unit ID. Returns Listing if found, null otherwise.
    public Listing getListing (String rentalUnitID) {
        if (!_cachedListings.isEmpty() && rentalUnitID != null) {
            for (Listing cachedListing : _cachedListings){
                if (rentalUnitID.equals(cachedListing.getRentalUnitID()))
                    return cachedListing;
            }
        }
        return null;
    }

    // Searches for all cached Listings by User who owns them. Returns a list of Listings associated with that User.
    public Listing[] getListings (User user) {
        List<Listing> searchResults = new ArrayList<Listing>();
        if (!_cachedListings.isEmpty() && user != null) {
            for (Listing cachedListing : _cachedListings){
                if (user == cachedListing.getOwner())
                    searchResults.add(cachedListing);
            }
        }
        return searchResults.toArray(new Listing[searchResults.size()]);
    }

    // Searches the cached Listings for Listings that match the passed city, rental price, and number of rooms, and returns them in a Listing[] array.
    // A city passed as "*" is equivalent to "any"
    public Listing[] searchListings (String city,  double rentalPrice, int numberOfRooms) {
        List<Listing> searchResults = new ArrayList<Listing>();
        if (!_cachedListings.isEmpty()) {
            for (Listing cachedListing : _cachedListings){
                if ((city.equals("*")? true : cachedListing.getCity().equals(city)) &&
                    (cachedListing.getRentalPrice() <= rentalPrice) &&
                    (cachedListing.getNumberOfRooms() >= numberOfRooms) &&
                    (!cachedListing.isRented()))
                        searchResults.add(cachedListing);
            }
        }
        return searchResults.toArray(new Listing[searchResults.size()]);
    }

    // Adds a User to the cached Users list; returns false if the username already exists
    public boolean addUser (User newUser) { 
        return (newUser != null && getUser(newUser.getUsername()) == null) ? this._cachedUsers.add(newUser) : false;
    }

    // Adds a Listing to the cached Listings list; returns false if the listing ID already exists
    public boolean addListing (Listing newListing) { 
        return (newListing != null && getListing(newListing.getRentalUnitID()) == null) ? this._newListings.add(newListing) : false;
    }

    // Adds a Log to the current session's Log list; returns true if the log was added successfully
    public boolean addLog (Log newLog) { 
        return newLog != null? this._sessionLogs.add(newLog) : false;
    }

    // Removes passed User object from the cached Users list
    public boolean removeUser(User user) { return this._cachedUsers.remove(user); }

    // Removes all Listings in the passed Listing array from the cached Listings
    public boolean removeListings(Listing[] listings) { return this._cachedListings.removeAll(Arrays.asList(listings)); }

    // Adds this session's newly created Listings to the cached Listing list
    public boolean commitNewListings () { 
        boolean addSuccess = this._cachedListings.addAll(this._newListings);
        this._newListings.clear();
        return addSuccess;
    }

    // Returns true if a Listing exists in the cached Listings list with the passed rental unit ID, otherwise returns false
    public boolean listingExists(String rentalUnitID) { return this.getListing(rentalUnitID) != null; }

    // Returns true if a User exists in the cached Users list with the passed username, otherwise returns false
    public boolean userExists(String username) { return this.getUser(username) != null; }

    // Returns true if the cached Users list has been previously loaded during the process lifetime, otherwise returns false
    public boolean areUsersLoaded() { return _usersAreLoaded; }

    // Returns true if the cached Listings list has been previously loaded during the process lifetime, otherwise returns false 
    public boolean areListingsLoaded() { return _listingsAreLoaded; }

    // Reads the users.txt file and creates Users from the contents, loading them into the cached Users list.
    public void loadUsers() throws IOException{
        File users = new File(this._usersLocation);
        Scanner reader = new Scanner(users);
        while (reader.hasNextLine()){
            String data = reader.nextLine();
            if (data.equals("END"))
                break;
             _cachedUsers.add(new User( data.substring(0, 15).trim(),                               // Username
                                        User.UserType.fromString(data.substring(16, 18))            // User Type
             ));
        }
        reader.close();
        _usersAreLoaded = true;
    }

    // Reads the listings.txt file and creates Listings from the contents, loading them into the cached Listings list
    public void loadListings() throws IOException, IllegalStateException {
        if (!areUsersLoaded())
            throw new IllegalStateException("Users must be loaded prior to Listings!");

        File listings = new File(this._listingsLocation);
        Scanner reader = new Scanner(listings);

        while (reader.hasNextLine()){
            String data = reader.nextLine();
            if (data.equals("END"))
                break;
            _cachedListings.add(new Listing( data.substring(0, 8),                                  // Listing ID
                                             getUser(data.substring(9, 24).trim()),                 // User ID of owner
                                             data.substring(25, 50).trim(),                         // City
                                             Double.parseDouble(data.substring(53, 59)),            // Price per Night
                                             Integer.parseInt(data.substring(51, 52).trim()),       // Number of Rooms
                                             (data.substring(60, 61).equals("T") ? true : false),   // rental flag
                                             Integer.parseInt(data.substring(62, 64))               // Number of nights remaining
            ));
        }
        reader.close();
        _listingsAreLoaded = true;
    }

    // Appends the session’s Logs to the daily transaction file, then clears the Log list 
    // If a transaction file does not exist for today, a new file is created with the local date as its name
    public void writeDailyTransactionFile() throws IOException {
        File transactionFile = new File(_transactionFileLocation + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd")) + ".log");

        if (!transactionFile.exists())
            transactionFile.createNewFile();

        BufferedWriter buffer = new BufferedWriter(new FileWriter(transactionFile, true));
        for (Log log : _sessionLogs){
            buffer.write(log.getTransactionCode().toString() + " " +                                // Transaction Code
                         String.format("%-15s", log.getUsername()) + " " +                          // Username
                         log.getUserType().toString() + " " +                                       // User Type
                         String.format("%-8s", log.getRentalUnitID()) + " " +                       // Rental Unit ID
                         String.format("%-25s", log.getCity()) + " " +                              // City
                         log.getNumberOfRooms() + " " +                                             // Number of Rooms
                         (new DecimalFormat("000.00")).format(log.getRentalPrice()) + " " +         // Price per night
                         String.format("%02d", log.getNightsRented())                               // Nights Rented
            );
            buffer.newLine();
        }
        buffer.close();
        _sessionLogs.clear();
    }
}
