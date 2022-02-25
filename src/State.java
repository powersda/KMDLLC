import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public abstract class State {

    // Extend this class and implement this method
    protected abstract User execute(User activeUser, DataAccess dbHandle, Scanner inputSource) throws SecurityException;
    protected static void showBanner() {
        System.out.print("*****************************************************\n" +
                         "* Welcome to the OT-BnB listing management program! *\n" +
                         "*****************************************************\n");
    }

    // Defines allowable commands
    public enum Command { 
        QUIT("quit"), LOGIN("login"), LOGOUT("logout"), CREATE("create"), DELETE("delete"), POST("post"), SEARCH("search"), RENT("rent");
        private final String _commandString;
        private Command(String commandString) { this._commandString = commandString; }
        public String toString() { return _commandString; }
        protected Boolean validateUser(User activeUser){ return Command.getUserPermissions(activeUser).contains(this); }

        private static Map<Command, User.UserType[]> _permissions = new LinkedHashMap<>();
        static {
            _permissions.put(CREATE, new User.UserType[] {User.UserType.ADMIN});
            _permissions.put(DELETE, new User.UserType[] {User.UserType.ADMIN});
            _permissions.put(LOGIN, new User.UserType[]{null});
            _permissions.put(POST, new User.UserType[] {User.UserType.ADMIN, User.UserType.FULL_STANDARD, User.UserType.POST_STANDARD});
            _permissions.put(SEARCH, new User.UserType[] {User.UserType.ADMIN, User.UserType.FULL_STANDARD, User.UserType.POST_STANDARD, User.UserType.RENT_STANDARD});
            _permissions.put(RENT, new User.UserType[] {User.UserType.ADMIN, User.UserType.FULL_STANDARD, User.UserType.RENT_STANDARD});
            _permissions.put(QUIT, new User.UserType[]{null});           
            _permissions.put(LOGOUT, new User.UserType[] {User.UserType.ADMIN, User.UserType.FULL_STANDARD, User.UserType.POST_STANDARD, User.UserType.RENT_STANDARD});
        }

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
