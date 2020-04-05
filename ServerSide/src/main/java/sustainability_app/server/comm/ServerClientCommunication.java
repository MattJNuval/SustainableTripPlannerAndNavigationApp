package sustainability_app.server.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.here.flexpolyline.PolylineEncoderDecoder.LatLngZ;

/**
 * Communication for server and client using sockets.
 */
public final class ServerClientCommunication {
    private final static int CONNECTION_THREAD_SLEEP = 0;
    private final static int HERE_ALTERNATIVES = 6;
    private final static String HERE_TRANSPORT_MODE = "truck";
    private final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private final String airVisualAPIKey;
    private final String hereApiKey;
    private final ServerSocket serverSocket;

    /**
     * Constructor for a server and client communication.
     * @param portNumber {@link int} for the port number.
     * @param airVisualAPIKey (@link String} for the Air Visual API Key.
     * @param hereApiKey (@link String} for the HERE API Key.
     * @throws IOException if an IO error occurred.
     */
    public ServerClientCommunication(final int portNumber, final String airVisualAPIKey,
            final String hereApiKey) throws IOException {
        this.airVisualAPIKey = airVisualAPIKey;
        this.hereApiKey = hereApiKey;
        serverSocket = new ServerSocket(portNumber);
        serverSocket.setSoTimeout(0);
    }
    
    private final class ClientThread
        extends Thread {
        private final DataInputStream dis; 
        private final DataOutputStream dos;
        private final Socket socket;
        
        public ClientThread(final Socket socket,
                final DataInputStream dis, final DataOutputStream dos) {
            this.dis = dis;
            this.dos = dos;
            this.socket = socket;
        }
        
        @Override
        public void run() { // Threading for each client.
            if (dis != null && dos != null && socket != null) {
                while (true) {
                    try {
                        // Data received from client.
                        final String received = dis.readUTF();
                        // Data to send to client.
                        final JSONObject toReturnJSON = new JSONObject();
                        // Time stamp to send to client.
                        toReturnJSON.put("ts", java.time.Clock.systemUTC().instant());
                        
                        LOGGER.log(Level.INFO, "Client " + socket + " sent " + received);
                        
                        final JSONTokener tokener = new JSONTokener(received);
                        // Answer from client.
                        final JSONObject answer = new JSONObject(tokener);
                        
                        // Command from client.
                        final String command = answer.getString("clientCommand");
                        
                        if (command.equals("exit")) { // Client exit command.
                            socket.close();
                            LOGGER.log(Level.INFO, "Client " + socket + " disconnected.");
                         // Breaks the connection.
                            break;
                        }
                        else if (command.equals("ping")) { // Client ping command.
                            // Command to send to client.
                            toReturnJSON.put("serverCommand", "pong");
                            LOGGER.log(Level.INFO, "Client " + socket + " pinged server.");
                        }
                        else if (command.equals("route-get")) { // Client route-get command.
                            routeGet(socket, toReturnJSON, answer);
                        }
                        
                        dos.writeUTF(toReturnJSON.toString());
                        LOGGER.log(Level.INFO, "Sending to client " + socket
                                + ": " + toReturnJSON.toString());
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.WARNING, "Client " + socket + " sent a bad formatted message.", e);
                    } catch (JSONException e) {
                        LOGGER.log(Level.WARNING, "Client " + socket + " sent a bad formatted message.", e);
                    } catch (URISyntaxException e) {
                        LOGGER.log(Level.WARNING, "URI has a bad format.", e);
                    } catch (IOException e) {
                        LOGGER.log(Level.SEVERE, "Client " + socket + " connection error.", e);
                        break;
                    } catch (InterruptedException e) {
                        LOGGER.log(Level.SEVERE, "Thread interrupted.", e);
                    }
                }
                
                // Close connection resources.
                try {
                    dis.close();
                    dos.close();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Could not properly close resources.", e);
                } 
            }   
        }
    }
    
    /**
     * Closes the server socket.
     * @return {@link ServerClientCommunication} of this.
     * @throws IOException if an IO error occurred.
     */
    public ServerClientCommunication close()
            throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
        return this;
    }
    
    /**
     * Listens on server socket.
     * @return {@link ServerClientCommunication} of this.
     * @throws IOException if an IO error occurred.
     */
    public ServerClientCommunication listen()
            throws IOException {
        if (serverSocket != null) {
            final Socket clientSocket = serverSocket.accept();
            LOGGER.log(Level.INFO, "Client connected: " + clientSocket);
            final DataInputStream dis = new DataInputStream(clientSocket.getInputStream()); 
            final DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream()); 
            
            LOGGER.log(Level.INFO, "Assigning new thread for client " + clientSocket); 
            final Thread t = new ClientThread(clientSocket, dis, dos); 
            t.start();    
        }
        
        return this;
    }
    
    /**
     * Processes route-get command.
     * @param socket {@link Socket} for the socket communication.
     * @param toReturnJSON {@link JSONObject} for the returning JSON.
     * @param answer {@link JSONObject} for the answering JSON.
     * @return {@link JSONObject} of the returning JSON.
     * @throws JSONException if a JSON error occurred.
     * @throws IOException if an IO error occurred.
     * @throws URISyntaxException if URL has a syntax error.
     * @throws InterruptedException if the thread was interrupted.
     */
    public JSONObject routeGet(final Socket socket, final JSONObject toReturnJSON,
            final JSONObject answer) throws JSONException, IOException,
            URISyntaxException, InterruptedException {
        // Command to send to client.
        toReturnJSON.put("serverCommand", "route-send");
        // Origin latitude to start at.
        final String originLat = answer.getString("originLat");
        toReturnJSON.put("originLat", originLat);
        // Origin longitude to start at.
        final String originLon = answer.getString("originLon");
        toReturnJSON.put("originLon", originLon);
        final LatLngZ origin = new LatLngZ(new Double(originLat), new Double(originLon));
        
        // Destination latitude to end at.
        final String destinationLat = answer.getString("destinationLat");
        // Destination longitude to end at.
        final String destinationLon = answer.getString("destinationLon");
        final LatLngZ destination = new LatLngZ(new Double(destinationLat), new Double(destinationLon));
        
        LOGGER.log(Level.INFO, "Client " + socket + " asking for route from "
        + origin + " to " + destination);

        LOGGER.log(Level.INFO, "Will try to send to client " + socket
                + " " + HERE_ALTERNATIVES + " " 
                + HERE_TRANSPORT_MODE + " routes.");
        
        // This is where things get tricky.
        // 1. Get all possible routes.
        // 2. Put all route coordinates in their own json objects.
        // 3. Get air pollution from each route coordinate.
        // 4. Sum all the air pollution from each route.
        // 5. Label best route.
        
        // TODO: Add truck information.
        
        JSONObject leastPollutedRoute = null;
        double lastAqi = 0;
        
        // Routes from coordinates and their polylines.
        final HERERoute routeFetch = new HERERoute(this.hereApiKey, origin,
                destination, HERE_TRANSPORT_MODE,
                HERE_ALTERNATIVES, "polyline");

        for (int i = 0; i < routeFetch.routeArray().length(); i++) {
            final JSONObject routeJSON = new JSONObject();
            final List<LatLngZ> polyline = routeFetch.polyline(i, 0);
            double totalRouteAqi = 0;
            
            // Gets all coordinates from polyline.
            for (int j = 0; j < polyline.size(); j++) {
                final LatLngZ coordinate = polyline.get(j);
                final JSONObject coordinateJSON = new JSONObject();
                coordinateJSON.put("lat", coordinate.lat);
                coordinateJSON.put("lon", coordinate.lng);
                coordinateJSON.put("z", coordinate.z);
                try {
                    // AQIUS from each coordinate.
                    final AirVisualAQI aqiFetch = new AirVisualAQI(this.airVisualAPIKey,
                            coordinate);
                    coordinateJSON.put("aqi", aqiFetch.AQIUS().doubleValue());
                    coordinateJSON.put("aqi", 0);
                    
                    // Add AQIUS to total AQIUS for route.
                    totalRouteAqi += aqiFetch.AQIUS().doubleValue();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Failed to get coordinate AQIUS for route.", e);
                    coordinateJSON.put("aqi", "?");
                }
                routeJSON.put("c" + j, coordinateJSON);
                
                // Prevent as much overhead as possible.
                Thread.sleep(CONNECTION_THREAD_SLEEP);
            }
            
            routeJSON.put("totalRouteAqi", totalRouteAqi);
            
            // Calculate least polluted route.
            if (lastAqi == 0 || lastAqi > totalRouteAqi) {
                leastPollutedRoute = routeJSON;
                lastAqi = totalRouteAqi;
            }
            
            // Add route.
            // toReturnJSON.put("route" + i, routeJSON);
        }
        
        toReturnJSON.put("leastAqiRoute", leastPollutedRoute);
        
        return toReturnJSON;
    }
}