package com.example.sustainabilityapp;

import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
    private static final String CLIENT = "CLIENT";
    private Socket socket = null;
    private DataInputStream dataInputStream = null;
    private DataOutputStream dataOutputStream = null;
    private String address;
    private int port;
    private String input;

    public Client(String address, int port, String input) {
       this.address = address;
       this.port = port;
       this.input = input;
    }

    @Override
    public void run() {
        // Establish a connection
        try {
            socket = new Socket(address,port);
            Log.i(CLIENT,"Connected");

            // Takes input from terminal
            dataInputStream = new DataInputStream(socket.getInputStream());

            // Sends output to the socket
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            dataOutputStream.writeUTF(input);
            String result = dataInputStream.readUTF();
            Log.i(CLIENT, result + "");
            dataOutputStream.writeUTF("{\"clientCommand\": \"exit\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}