import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;

public class ServerCommunication {
    private Thread listeningThread;
    private int portNumber;
    private ServerSocket serverSocket;
    private Map<Socket, DataInputStream> socketMap;

    public ServerCommunication(final int portNumber) {
        this.portNumber = portNumber;
        socketMap = new Hashtable<Socket, DataInputStream>();
    }
    
    public ServerCommunication close() {
        if (listeningThread != null && serverSocket != null && socketMap != null) {
            listeningThread.interrupt();
            System.out.println("Closing server connections.");
            for (Map.Entry<Socket, DataInputStream> entry : socketMap.entrySet()) {
                if (entry.getKey() != null && (entry.getKey().isConnected()
                        && !entry.getKey().isClosed())) {
                    if (entry.getValue() != null) {
                        try {
                            entry.getValue().close();
                        } catch (IOException ex) {
                            // TODO Auto-generated catch block
                            ex.printStackTrace();
                        }
                    }
                    try {
                        entry.getKey().close();
                    } catch (IOException ex) {
                        // TODO Auto-generated catch block
                        ex.printStackTrace();
                    }
                    System.out.println("Client connection closed: " + entry.getKey());
                    socketMap.remove(entry.getKey());
                }
            }
            socketMap.clear();
            
            try {
                serverSocket.close();
            } catch (IOException ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
        }
        return this;
    }
    
    public Map<String, String> data() {
        final Map<String, String> dataMap = new Hashtable<String, String>();
        if (socketMap != null) {
            for (Map.Entry<Socket, DataInputStream> entry : socketMap.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    try {
                        dataMap.put(entry.getKey().toString(), entry.getValue().readUTF());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return dataMap;
    }
    
    public ServerCommunication listen() {
        listeningThread = new Thread() {
            @Override
            public void run() {
                    try {
                        serverSocket = new ServerSocket(portNumber);
                        System.out.println("Waiting for clients.");
                        while (!Thread.currentThread().isInterrupted()) {
                            for (Map.Entry<Socket, DataInputStream> entry : socketMap.entrySet()) {
                                if (entry.getKey() != null && (!entry.getKey().isConnected()
                                        || entry.getKey().isClosed())) {
                                    if (entry.getValue() != null) {
                                        entry.getValue().close();
                                    }
                                    System.out.println("Client connection closed: " + entry.getKey());
                                    socketMap.remove(entry.getKey());
                                }
                            }
                            
                            final Socket socket = serverSocket.accept();
                            System.out.println("Client accepted: " + socket);
                            final DataInputStream in =
                                    new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                            socketMap.put(socket, in);
                        }
                        
                    } catch (IOException ex) {
                        // TODO Auto-generated catch block
                        ex.printStackTrace();
                    }
            }
        };
        listeningThread.start();
        return this;
    }
}
