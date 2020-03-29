package sustainability_app.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sustainability_app.server.comm.ServerClientCommunication;

/**
 * Driver for main entry.
 */
public class Driver {
    private final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Main entry point. This requires two arguments: one integer number for the port, the other a boolean for testing.
     * @param args {@link String[]} for arguments for the application.
     */
    public static void main(final String[] args) {
        if (args.length >= 1) {
            final int portNumber = Integer.parseInt(args[0]);
            LOGGER.log(Level.INFO, "CloudJam 2020 Sustainable Trip Planner and Navigation "
                    + "App Server starting on port " + portNumber + ".");

            // For sockets using specified port number given.
            ServerClientCommunication serverClientCommunication = null;
            
            try {
                serverClientCommunication = new ServerClientCommunication(portNumber);
                LOGGER.log(Level.INFO, "Ready.");
                if (Boolean.parseBoolean(args[1])) { // For testing purposes.
                    serverClientCommunication.test();
                }
                else {
                    while (true) {
                        // Listen on port for socket.
                        serverClientCommunication.listen();
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Server error.", e);
            } finally {
                try {
                    if (serverClientCommunication != null) {
                        // Close socket when done.
                        serverClientCommunication.close(); 
                    }
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