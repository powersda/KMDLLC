import java.util.Scanner;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {   
        DataAccess dbHandle = args.length == 3 ? new DataAccess(args[0], args[1], args[2]) :  new DataAccess();
        Scanner inputSource = new Scanner(System.in);
        User activeUser = null;
        State.showBanner();

        mainMenuLoop:
        while (true){
            System.out.print("Please enter one of the following commands: " + (State.Command.getUserPermissions(activeUser)) + ": ");
            try {
                State newState = null;
                switch(State.Command.valueOf(inputSource.nextLine().toUpperCase())){
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
                        if (!State.Command.QUIT.validateUser(activeUser))
                                throw new SecurityException("Please logout before using the \"" + State.Command.QUIT + "\" command!");
                        break mainMenuLoop;
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
        inputSource.close();
     }
}
