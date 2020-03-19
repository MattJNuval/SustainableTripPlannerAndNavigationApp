import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static Socket socket;
	private static int portNumber = 55555;
	private static ServerSocket serverSocket;
	private static ServerThread stObject;

	public Server() {
		this.startListening(this);
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public static void startListening(Server tempServer) {
		Server server = tempServer;
		Thread listeningThread = new Thread() {
			@Override
			public void run() {
				try {
					serverSocket = new ServerSocket(portNumber);
					while (true) {
						socket = serverSocket.accept();
						System.out.println("Client Accepted");
					}
				} catch (IOException ex) {
					System.out.println("IOException")
				}}};
				listeningThread.start();
			}
