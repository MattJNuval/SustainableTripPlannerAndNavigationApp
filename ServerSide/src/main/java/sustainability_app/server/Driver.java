package sustainability_app.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.here.flexpolyline.PolylineEncoderDecoder.LatLngZ;

import sustainability_app.server.air_visual_api.AirVisualAQI;
import sustainability_app.server.comm.ServerClientCommunication;
import sustainability_app.server.here_api.HERERoute;

public class Driver {
    public final static String AIR_VISUAL_API_KEY =
            "d7664ac9-d9fb-4ed4-b6f6-2e8feac28693";
    public final static String HERE_API_KEY =
            "ZOBTtCPG_WoP8VHh-xDXFdekw0AzdKkF9S5gGvZkxDY";
    public final static String HERE_TRANSPORT_MODE = "truck";
    public final static String HERE_ALTERNATIVES = "6";
    public final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private ServerClientCommunication serverClientCommunication;
    
    public Driver() { }
    
    public static void main(final String[] args) throws IllegalArgumentException {
        
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
        
        // Testing HERE API
        /*try {
            final JSONObject toReturnJSON = new JSONObject();
            
            toReturnJSON.put("serverCommand", "route-give");
            final String originLat = "52.5308";
            toReturnJSON.put("originLat", originLat);
            final String originLon = "13.3847";
            toReturnJSON.put("originLon", originLon);
            final LatLngZ origin = new LatLngZ(new Double(originLat), new Double(originLon));
            final String destinationLat = "52.5323";
            final String destinationLon = "13.3789";
            final LatLngZ destination = new LatLngZ(new Double(destinationLat), new Double(destinationLon));
            
            Driver.LOGGER.log(Level.INFO, "Client asking for route from "
            + origin + " to " + destination);

            Driver.LOGGER.log(Level.INFO, "Will try to send to client a "
            + Driver.HERE_TRANSPORT_MODE + " route " + " with "
                    + Driver.HERE_ALTERNATIVES + " alternative routes.");
            
            // This is where things get tricky.
            // 1. Get all possible routes.
            // 2. Put all route coordinates in their own json objects.
            // 3. Get air pollution from each route coordinate.
            // 4. Sum all the air pollution from each route.
            // 5. Label best route.
            
            // TODO: Add truck information.
            
            final HERERoute routeFetch = new HERERoute(Driver.HERE_API_KEY, origin,
                    destination, Driver.HERE_TRANSPORT_MODE,
                    Driver.HERE_ALTERNATIVES, "polyline");
            
            Driver.LOGGER.log(Level.INFO, "Will try to send to client "
                    + " " + Driver.HERE_ALTERNATIVES + " " 
                    + Driver.HERE_TRANSPORT_MODE + " routes.");
            
            JSONObject leastPollutedRoute = null;
            double lastAqi = 0;
            
            for (int i = 0; i < routeFetch.routeArray().length(); i++) {
                final JSONObject routeJSON = new JSONObject();
                final List<LatLngZ> polyline = routeFetch.polyline(i, 0);
                double totalRouteAqi = 0;                        
                for (int j = 0; j < polyline.size(); j++) {
                    final LatLngZ coordinate = polyline.get(j);
                    final JSONObject coordinateJSON = new JSONObject();
                    coordinateJSON.put("lat", coordinate.lat);
                    coordinateJSON.put("lon", coordinate.lng);
                    coordinateJSON.put("z", coordinate.z);
                    try {
                        final AirVisualAQI aqiFetch = new AirVisualAQI(Driver.AIR_VISUAL_API_KEY,
                                 coordinate);
                        
                        coordinateJSON.put("aqi", aqiFetch.AQIUS());                        
                        totalRouteAqi += aqiFetch.AQIUS().doubleValue();
                    } catch (IOException e) {
                        Driver.LOGGER.log(Level.WARNING, "TEST.", e);
                        coordinateJSON.put("aqi", 0);  
                    }
                    routeJSON.put("c" + j, coordinateJSON);
                    Thread.sleep(5000);
                }
                
                routeJSON.put("totalRouteAqi", totalRouteAqi);
                
                if (lastAqi == 0 || lastAqi > totalRouteAqi) {
                    leastPollutedRoute = routeJSON;
                    lastAqi = totalRouteAqi;
                }
                
                toReturnJSON.put("route" + i, routeJSON);
            }
            
            toReturnJSON.put("leastAqiRoute", leastPollutedRoute);
            Driver.LOGGER.log(Level.INFO, "Sending to client: " + toReturnJSON.toString());
        } catch (JSONException e) {
            Driver.LOGGER.log(Level.WARNING, "Test.", e);
        } catch (URISyntaxException e) {
            Driver.LOGGER.log(Level.WARNING, "Test.", e);
        } catch (IOException e) {
            Driver.LOGGER.log(Level.SEVERE, "Test.", e);
        } catch (InterruptedException e) {
            Driver.LOGGER.log(Level.WARNING, "Test.", e);
        }*/
    }
}