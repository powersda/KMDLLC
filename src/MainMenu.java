 /***************************************************************************************************************************************************************************
*
* Program Name:     OT-Bnb
*
* Author:           KDM LLC
*
* Description:      A program designed to create, search, and rent listings. Also provides user management functionality to optionally create and delete users
*                   with limited access to listing-related functions. This program reads a user file and listings file (located at "data/users.txt" and 
*                   "data/listings.txt", respectively) for processing at user login, then writes a record of the session's transactions to the "log/" directory when 
*                   the user logs out. All transaction records for a given day are written to the same file, which will be processed by a separate
*                   application at day's end to update the "user.txt" and "listings.txt" files for the following day.
*
* Input Files:      data/listings.txt, data/users.txt
*
* Output Files:     added to /log directory, with names indicating their creation date
*
* Run Instructions: Double click the "OT-Bnb" jar file, or run "java -jar OT-Bnb.jar" from a command prompt. Note: the program searches for input files based on the
*                   user's current directory, so running the application from within the same directory is advised. Custom input and output file locations can be specified
*                   by running the command with custom parameters in the following format: java -jar OT-Bnb.jar <logDirectoryLocation> <usersFileLocation> <listingFileLocation>
*
 /***************************************************************************************************************************************************************************/


/*******************************
* Class: Main
* Description: Extends the State class to present a main menu that provides the user with access to various program functions. Also contains the program entry point.
********************************/

import java.util.Scanner;
import java.io.FileNotFoundException;

public class MainMenu extends State {

    // Implements the execute method from State to present a main menu, then launch other States based on console input.
    public User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) {   
        showBanner();

        while (true){
            System.out.print("Please enter one of the following commands: " + (Command.getUserPermissions(activeUser)) + ": ");
            try {
                State newState = null;
                switch(Command.valueOf(inputSource.nextLine().trim().toUpperCase())){
                    case LOGIN:
                        newState = new Login();
                        break;
                    case CREATE:
                        newState= new CreateUser();
                        break;
                    case DELETE:
                        newState= new DeleteUser();
                        break;
                    case POST:
                        newState = new PostListing();
                        break;
                    case SEARCH:
                        newState = new SearchListing();
                        break;
                    case RENT:
                        newState = new RentListing();
                        break;
                    case LOGOUT:
                        newState = new Logout();
                        break;
                    case QUIT:
                        if (!Command.QUIT.validateUser(activeUser))
                                throw new SecurityException("Please logout before using the \"" + Command.QUIT + "\" command!");
                        return activeUser;
                }
                activeUser = newState.execute(activeUser, dbHandle, inputSource);
            }
            catch (IllegalArgumentException exception){
                System.out.println("Command not recognized.");
            }
            catch (SecurityException exception){
                System.out.println(exception.getMessage());
            }
        }
     }

    // Program entry point: initializes the input device and DataAccess object, then launches the main menu
     public static void main(String[] args) {
        Scanner inputSource = new Scanner(System.in);
        (new MainMenu()).execute(null, (args.length == 3 ? new DataAccess(args[0], args[1], args[2]) :  new DataAccess()), inputSource);
        inputSource.close();
    }
}
