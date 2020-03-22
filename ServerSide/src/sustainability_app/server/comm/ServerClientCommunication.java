package sustainability_app.server.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ServerClientCommunication {
    private final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
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
        
        public ClientThread(final Socket socket, final DataInputStream dis, final DataOutputStream dos) {
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
                        String toReturn = null;
                        LOGGER.log(Level.INFO, "Client " + socket + " said: " + received);
                        if (received.equals("Exit"))
                        {
                            socket.close();
                            LOGGER.log(Level.INFO, "Client " + socket + " disconnected.");
                            break;
                        }
                        else if (received.equals("Ping")) {
                            toReturn = "Pong";
                            LOGGER.log(Level.INFO, "Client " + socket + " pinged server.");
                            dos.writeUTF(toReturn);
                        }
                    } catch (IOException e) {
                        LOGGER.log(Level.WARNING, "Client connection error.", e);
                        break;
                    }
                }
                
                try
                {
                    dis.close();
                    dos.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Could not properly close resources.", e);
                } 
            }   
        }
    }
    
    public ServerCommunication close()
            throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
        return this;
    }
    
    public ServerCommunication listen()
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
