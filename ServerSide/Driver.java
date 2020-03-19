import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Driver {
    private final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private ServerCommunication serverCommunication;
    
    public Driver() { }
    
    public static void main(final String[] args)
            throws IllegalArgumentException {
        
        if (args.length >= 1) {
            final int portNumber = Integer.parseInt(args[0]);
            LOGGER.log(Level.INFO, "CloudJam 2020 Sustainable Trip Planner and Navigation "
                    + "App Server starting on port " + portNumber + ".");
            
            final Driver driver = new Driver();
            
            try {
                driver.serverCommunication = new ServerCommunication(portNumber);
                LOGGER.log(Level.INFO, "Ready");
                while (true) {
                    driver.serverCommunication.listen();
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Server error.", e);
            } finally {
                try {
                    driver.serverCommunication.close();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Could not close server.", e);
                }
            }
        } else {
            final IllegalArgumentException illegal = new IllegalArgumentException("Wrong number of arguments.");
            LOGGER.log(Level.SEVERE, "Wrong number of arguments.", illegal);
            throw illegal;
        }
    }
}