package sustainability_app.server.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.here.flexpolyline.PolylineEncoderDecoder.LatLngZ;

import sustainability_app.server.Driver;
import sustainability_app.server.here_api.HERERoute;

public final class ServerClientCommunication {    
    private final ServerSocket serverSocket;

    public ServerClientCommunication(final int portNumber)
            throws IOException {
        serverSocket = new ServerSocket(portNumber);
        serverSocket.setSoTimeout(0);
    }
    
    protected final class ClientThread
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
        public void run() {
            if (dis != null && dos != null && socket != null) {
                while (true) {
                    try {
                        final String received = dis.readUTF();
                        final JSONObject toReturnJSON = new JSONObject();
                        Driver.LOGGER.log(Level.INFO, "Client " + socket + " sent " + received);
                        
                        final JSONTokener tokener = new JSONTokener(received);
                        final JSONObject answer = new JSONObject(tokener);
                        
                        final String command = answer.getString("client-command");
                        
                        if (command.equals("exit")) {
                            socket.close();
                            Driver.LOGGER.log(Level.INFO, "Client " + socket + " disconnected.");
                            break;
                        }
                        else if (command.equals("ping")) {
                            toReturnJSON.put("server-command", "pong");
                            Driver.LOGGER.log(Level.INFO, "Client " + socket + " pinged server.");
                            dos.writeUTF(toReturnJSON.toString());
                        }
                        else if (command.equals("route")) {
                            final String originLat = answer.getString("originLat");
                            final String originLon = answer.getString("originLon");
                            final LatLngZ origin = new LatLngZ(new Double(originLat), new Double(originLon));
                            final String destinationLat = answer.getString("destinationLat");
                            final String destinationLon = answer.getString("destinationLon");
                            final LatLngZ destination = new LatLngZ(new Double(destinationLat), new Double(destinationLon));
                            
                            Driver.LOGGER.log(Level.INFO, "Client " + socket + " asking for route from "
                            + origin + " to " + destination);

                            Driver.LOGGER.log(Level.INFO, "Will try tp send to client a " + Driver.HERE_TRANSPORT_MODE
                                    + " route " + " with " + Driver.HERE_ALTERNATIVES + " alternative routes.");
                            
                            // This is where things get tricky.
                            // 1. Get all possible routes.
                            // 2. Put all route coordinates in their own hashmaps.
                            // 3. Get air pollution from each route coordinate.
                            // 4. Sum all the hashmaps.
                            // 5. Label best hashmap.
                            
                            // TODO: Add truck information.
                            
                            final HERERoute routeTest = new HERERoute(Driver.HERE_API_KEY, origin,
                                    destination, Driver.HERE_TRANSPORT_MODE,
                                    Driver.HERE_ALTERNATIVES, "polyline");
                            
                            toReturnJSON.put("server-command", "route-give");
                            dos.writeUTF(toReturnJSON.toString());
                        }
                        
                        Driver.LOGGER.log(Level.INFO, "Sending to client " + socket
                                + ": " + toReturnJSON.toString());
                    } catch (NumberFormatException e) {
                        Driver.LOGGER.log(Level.WARNING, "Client sent a bad formatted message.", e);
                    } catch (JSONException e) {
                        Driver.LOGGER.log(Level.WARNING, "Client sent a bad formatted message.", e);
                    } catch (URISyntaxException e) {
                        Driver.LOGGER.log(Level.WARNING, "URI has internal error.", e);
                    } catch (IOException e) {
                        Driver.LOGGER.log(Level.WARNING, "Client connection error.", e);
                        break;
                    }
                }
                
                try
                {
                    dis.close();
                    dos.close();
                } catch (IOException e) {
                    Driver.LOGGER.log(Level.WARNING, "Could not properly close resources.", e);
                } 
            }   
        }
    }
    
    public ServerClientCommunication close()
            throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
        return this;
    }
    
    public ServerClientCommunication listen()
            throws IOException {
        if (serverSocket != null) {
            final Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket);
            final DataInputStream dis = new DataInputStream(clientSocket.getInputStream()); 
            final DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream()); 
            
            System.out.println("Assigning new thread for client " + clientSocket); 
            final Thread t = new ClientThread(clientSocket, dis, dos); 
            t.start();    
        }
        
        return this;
    }
}
