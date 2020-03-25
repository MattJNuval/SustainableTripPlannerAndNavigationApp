package sustainability_app.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sustainability_app.server.comm.ServerClientCommunication;

/**
 * Driver for main entry.
 * @author Christian
 *
 */
public class Driver {
    public final static String AIR_VISUAL_API_KEY =
            "d7664ac9-d9fb-4ed4-b6f6-2e8feac28693";
    public final static String HERE_API_KEY =
            "ZOBTtCPG_WoP8VHh-xDXFdekw0AzdKkF9S5gGvZkxDY";
    public final static String HERE_TRANSPORT_MODE = "truck";
    public final static String HERE_ALTERNATIVES = "6";
    public final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private Driver() { }
    
    /**
     * Main entry point. This requires one single argument for the port number in which the server must run.
     * @param args arguments to be inputted.
     * @throws IllegalArgumentException if no arguments are inputted.
     */
    public static void main(final String[] args) throws IllegalArgumentException {
        if (args.length >= 1) {
            final int portNumber = Integer.parseInt(args[0]);
            LOGGER.log(Level.INFO, "CloudJam 2020 Sustainable Trip Planner and Navigation "
                    + "App Server starting on port " + portNumber + ".");
            
            final Driver driver = new Driver();
            ServerClientCommunication serverClientCommunication = null;
            
            try {
                serverClientCommunication = new ServerClientCommunication(portNumber);
                LOGGER.log(Level.INFO, "Ready");
                while (true) {
                    serverClientCommunication.listen();
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Server error.", e);
            } finally {
                try {
                    serverClientCommunication.close();
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