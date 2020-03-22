package sustainability_app.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sustainability_app.server.comm.ServerClientCommunication;

public class Driver {
    private final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private ServerClientCommunication serverClientCommunication;
    
    public Driver() { }
    
    public static void main(final String[] args)
            throws IllegalArgumentException {
        
        if (args.length >= 1) {
            final int portNumber = Integer.parseInt(args[0]);
            LOGGER.log(Level.INFO, "CloudJam 2020 Sustainable Trip Planner and Navigation "
                    + "App Server starting on port " + portNumber + ".");
            
            final Driver driver = new Driver();
            
            try {
                driver.serverClientCommunication = new ServerClientCommunication(portNumber);
                LOGGER.log(Level.INFO, "Ready");
                while (true) {
                    driver.serverClientCommunication.listen();
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Server error.", e);
            } finally {
                try {
                    driver.serverClientCommunication.close();
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