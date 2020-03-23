package sustainability_app.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.json.JSONException;

import sustainability_app.server.comm.ServerClientCommunication;
import sustainability_app.server.here_api.HERERoute;

public class Driver {
    private final static String AIR_VISUAL_API_KEY =
            "d7664ac9-d9fb-4ed4-b6f6-2e8feac28693";
    private final static String HERE_API_KEY =
            "ZOBTtCPG_WoP8VHh-xDXFdekw0AzdKkF9S5gGvZkxDY";
    private final static String HERE_TRANSPORT_MODE = "truck";
    private final static String HERE_ALTERNATIVES = "6";
    private final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private ServerClientCommunication serverClientCommunication;
    
    public Driver() { }
    
    public static void main(final String[] args) throws IllegalArgumentException {
        
        /*if (args.length >= 1) {
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
        }*/
        
        // Testing HERE API
        try {
            final HERERoute routeTest = new HERERoute(HERE_API_KEY, "52.5308,13.3847",
                    "52.5323,13.3789", HERE_TRANSPORT_MODE, HERE_ALTERNATIVES, "polyline");
            // Get from route 0 (up to 6 routes?), section 0 (not sure how many sections there are)
            System.out.println(routeTest.polyline(0, 0));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}