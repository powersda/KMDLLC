import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter; 
import java.nio.file.FileSystems;
import java.nio.file.Paths;

/*******************************
* Class: BackendStub
* Description: Allows for the creation of randomized listings and users data
* for the dailySimulationScript & weeklySimulationScript scripts.
********************************/

public class BackendStub {
		
	public static void main(String[] args) {
		Listing generatedListing;
		int incrementedIndex;
        String curDir = Paths.get("data").toAbsolutePath().toString();
        int linesOfData = 100;
        int daysOfData = 1;
        File userTextFile = new File(curDir, "users.txt");
        File listingTextFile = new File(curDir, "listings.txt");

        // Outter forloop which creates x days worth of data and outputs them as files
        for (int i=0; i<daysOfData; i++) {
        	List<Listing> generatedListingList = new ArrayList<Listing>();
            List<User> generatedUsers = new ArrayList<User>();           

            // Inner forloop which creates x listings and users with randomized data
	        for (int z=0; z<linesOfData; z++) {

                // The first users for each day is an admin with the usersname User00i, with i incrementing per day, and have them create a listing
	        	if (z==0) {
	        		generatedUsers.add(CreateRandomAdmin(i));
	        		generatedListingList.add(new Listing(generatedUsers.get(z), CreateRandomCity(), CreateRandomPrice(), CreateRandomRoomNumber()));        	
	        	}else {
                    // For every other loop iteration, create a rent-standard user to rent the previous iterations listing,
                    // else create a user and a listing under that new users name
	        		if(z%2==0) {
	        			generatedUsers.add(CreateRandomRentUser());
		        		generatedListingList.add(new Listing(generatedUsers.get(z-1), CreateRandomCity(), CreateRandomPrice(), CreateRandomRoomNumber()));
		        		generatedListingList.get(z).setNightsRented(CreateRandomNightsRented());
		        		generatedListingList.get(z).setRentedFlag(true);
		        				        		
	        		}else {
		        		generatedUsers.add(CreateRandomUser());
		        		generatedListingList.add(new Listing(generatedUsers.get(z), CreateRandomCity(), CreateRandomPrice(), CreateRandomRoomNumber()));
		 	
	        		}
        		}
        	}
	        
            // Used to output data per day as text files
	        try {	
	        	userTextFile.delete();
	        	listingTextFile.delete();

                // Checks if the file already exists (should not exist, deleted above)
		        if (userTextFile.createNewFile()) {
			            System.out.println("File created: " + userTextFile.getName());
			            System.out.println("File created: " + listingTextFile.getName());

			            FileWriter userWriter = new FileWriter(userTextFile.getPath());
			            FileWriter listingWriter = new FileWriter(listingTextFile.getPath());
			            String formattedListing = "";
			            String formattedUsers = "";
			            String endLineListing = "END";
			            String endLineUser = "END";
			            String booleanString;

                        // Forloop to format and output the randomly generated data into a text file
				        for(int h = 0; h<linesOfData; h++) {
				        	if (generatedListingList.get(h).isRented() == true) {
				        		booleanString = "T";
				        	}else {
				        		booleanString = "F";
				        	}
				        	formattedListing = formattedListing.concat(
				        			generatedListingList.get(h).getRentalUnitID().toString() + " " + 
				        			generatedListingList.get(h).getOwner().getUsername().toString() + " " + 
				        			generatedListingList.get(h).getCity().toString() + " " + 
				        			generatedListingList.get(h).getNumberOfRooms() + " " + 
				        			String.format("%06.2f", generatedListingList.get(h).getRentalPrice()) + " " + 
				        			booleanString + " " + 
				        			String.format("%02d", generatedListingList.get(h).getNightsRented()) + "\n");
				        	 
				        	formattedUsers = formattedUsers.concat(
				        			 generatedUsers.get(h).getUsername().toString() + " " + 
				        			 generatedUsers.get(h).getUserType().toString() + "\n");
				        	 
				        }
				        formattedListing = formattedListing.concat(endLineListing);
				        formattedUsers = formattedUsers.concat(endLineUser);
				        listingWriter.write(formattedListing);
		        		userWriter.write(formattedUsers);
				        listingWriter.close();
				        userWriter.close();
		        } else {
		            System.out.println("File already exists.");
		        }
	        } catch (IOException e) {
	          System.out.println("An error occurred.");
	          e.printStackTrace();
	        }			
        }
    }

	// Used to create random valid admin data for the listings test
    protected static User CreateRandomAdmin(int index) {
    	index++;
        User admin;
        String username = "User" + String.format("%03d", index) + "        ";
        admin = new User(username, User.UserType.ADMIN); 
        return admin;
    }
    
	// Used to create random valid user data for the listings test
    protected static User CreateRandomUser() {
        Random rnd = new Random();
        List<User.UserType> userTypes = Arrays.asList(User.UserType.FULL_STANDARD, User.UserType.POST_STANDARD);
        User testUsers;
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder userID = new StringBuilder();
        while (userID.length() < 15) {
            int index = (int) (rnd.nextFloat() * CHARS.length());
            userID.append(CHARS.charAt(index));
        }
        String userIDStr = userID.toString();
        testUsers = new User(userIDStr, userTypes.get(rnd.nextInt(2))); 
        return testUsers;
    }
    
    
	// Used to create random valid user data for the listings test
    protected static User CreateRandomRentUser() {
        Random rnd = new Random();
        List<User.UserType> userTypes = Arrays.asList(User.UserType.FULL_STANDARD, User.UserType.RENT_STANDARD);
        User testUsers;
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder userID = new StringBuilder();
        while (userID.length() < 15) {
            int index = (int) (rnd.nextFloat() * CHARS.length());
            userID.append(CHARS.charAt(index));
        }
        String userIDStr = userID.toString();
        testUsers = new User(userIDStr, userTypes.get(rnd.nextInt(2))); 
        return testUsers;
    }

    // Used to create random valid city data for the listings test
    protected static String CreateRandomCity() {
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
        StringBuilder cityName = new StringBuilder();
        Random rnd = new Random();
        while (cityName.length() < 25) {
            int index = (int) (rnd.nextFloat() * CHARS.length());
            cityName.append(CHARS.charAt(index));
        }
        String cityNameStr = cityName.toString();
        return cityNameStr;
    }

    // Used to create random valid price data for the listings test
    protected static double CreateRandomPrice() {
        Random rnd = new Random();
        int numOfRooms = rnd.nextInt(99999);
        double randomPriceDouble = numOfRooms;
        randomPriceDouble = randomPriceDouble/100;
        return randomPriceDouble;
    }

    // Used to create random valid room number data for the listings test
    protected static int CreateRandomRoomNumber() {
        Random rnd = new Random();
        int numOfRooms = rnd.nextInt(8);
        numOfRooms++;
        return numOfRooms;
    }
    
    protected static int CreateRandomNightsRented() {
        Random rnd = new Random();
        int nightsRented = rnd.nextInt(13);
        nightsRented++;
        return nightsRented;
    }
}
