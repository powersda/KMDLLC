/*******************************
* Class: State 
* Description: An abstract class that represents a program state. Unifies data and methods used by all states, but requires state execution to be implemented
********************************/

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public abstract class State {

    // Abstract method containing unique state logic that must be implemented by child States
    protected abstract User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException;

    // Prints the application banner
    protected static void showBanner() {
        System.out.print("*****************************************************\n" +
                         "* Welcome to the OT-BnB listing management program! *\n" +
                         "*****************************************************\n");
    }

    // Enumeration that represents valid user commands 
    public enum Command { 
        QUIT("quit"), LOGIN("login"), LOGOUT("logout"), CREATE("create"), DELETE("delete"), POST("post"), SEARCH("search"), RENT("rent");

        private final String _commandString;
        private static Map<Command, User.UserType[]> _permissions = new LinkedHashMap<>();
        static {
            _permissions.put(CREATE, new User.UserType[] {User.UserType.ADMIN});
            _permissions.put(DELETE, new User.UserType[] {User.UserType.ADMIN});
            _permissions.put(LOGIN,  new User.UserType[] {null});
            _permissions.put(POST,   new User.UserType[] {User.UserType.ADMIN, User.UserType.FULL_STANDARD, User.UserType.POST_STANDARD});
            _permissions.put(SEARCH, new User.UserType[] {User.UserType.ADMIN, User.UserType.FULL_STANDARD, User.UserType.POST_STANDARD, User.UserType.RENT_STANDARD});
            _permissions.put(RENT,   new User.UserType[] {User.UserType.ADMIN, User.UserType.FULL_STANDARD, User.UserType.RENT_STANDARD});
            _permissions.put(QUIT,   new User.UserType[] {null});           
            _permissions.put(LOGOUT, new User.UserType[] {User.UserType.ADMIN, User.UserType.FULL_STANDARD, User.UserType.POST_STANDARD, User.UserType.RENT_STANDARD});
        }

        // Constructor: creates a Command with the appropriate string representation
        private Command(String commandString) { this._commandString = commandString; }

        // Returns the Command's string representation 
        public String toString() { return _commandString; }

        // Checks the permissions map to verify whether passed user is authorized to use the current Command
        protected boolean validateUser(User activeUser){ return Command.getUserPermissions(activeUser).contains(this); }

        // Builds and returns a list of Commands that the passed user is authorized to use
        public static List<Command> getUserPermissions(User activeUser) {
    
            List<Command> allowedCommands = new ArrayList<>();
            for (Map.Entry<Command, User.UserType[]> entry : _permissions.entrySet()){
                if (Arrays.asList(entry.getValue()).contains(activeUser == null? null : activeUser.getUserType()))
                    allowedCommands.add(entry.getKey());
            }
            return allowedCommands;
        }

    }
}
