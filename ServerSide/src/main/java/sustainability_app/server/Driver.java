package sustainability_app.server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import sustainability_app.server.comm.ServerClientCommunication;

/**
 * Driver for main entry.
 */
public class Driver {
    private final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Main entry point. This requires 4 arguments.
     * Argument 1: Port number.
     * Argument 2: Testing value.
     * Argument 3: Air Visual API Key.
     * Argument 4: HERE API Key.
     * @param args {@link String[]} for arguments for the application.
     */
    public static void main(final String[] args) {
        if (args.length >= 4) {
            LOGGER.log(Level.INFO, "Sustainable Trip Planner and Navigation "
                    + "App Server starting on port " + args.length + ".");
            final int portNumber = Integer.parseInt(args[0]);
            LOGGER.log(Level.INFO, "Sustainable Trip Planner and Navigation "
                    + "App Server starting on port " + portNumber + ".");

            // For sockets using specified port number given.
            ServerClientCommunication serverClientCommunication = null;
            
            try {
                serverClientCommunication = new ServerClientCommunication(portNumber, args[2],
                        args[3]);
                LOGGER.log(Level.INFO, "Ready.");
                if (Boolean.parseBoolean(args[1])) { // For testing purposes.
                    final JSONObject testAnswer = new JSONObject();
                    testAnswer.put("originLat", "52.53232637420297");
                    testAnswer.put("originLon", "13.378873988986015");
                    testAnswer.put("destinationLat", "52.53098367713392");
                    testAnswer.put("destinationLon", "13.384566977620125");
                    try {
                        final JSONObject test = serverClientCommunication.routeGet(null, new JSONObject(), testAnswer);
                        LOGGER.log(Level.INFO, "Testing results: " + test.toString());
                    } catch (JSONException | URISyntaxException | InterruptedException e) {
                        LOGGER.log(Level.SEVERE, "Server testing error.", e);
                    }
                }
                else {
                    while (true) {
                        // Listen on port for socket.
                        serverClientCommunication.listen();
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Server IO error.", e);
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