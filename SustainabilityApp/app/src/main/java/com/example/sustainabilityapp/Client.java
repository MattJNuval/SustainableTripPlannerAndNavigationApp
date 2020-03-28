package com.example.sustainabilityapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {

    private final static String CLIENT = "CLIENT";
    private String results = "";
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Socket socket;


    public Client(String input) {
        results = input;
    }


    @Override
    public void run() {
        try {

            // Establish the connection with server port number 55
            socket = new Socket("192.168.1.105", 5056);

            // Obtaining input and out streams
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            Log.i(CLIENT, dataInputStream.readUTF());

            // Send results to Server
            dataOutputStream.writeUTF(results);

            // Store date received form Server
            String received = dataInputStream.readUTF();

            Log.i(CLIENT, received + "");


        } catch (Exception e) {

            Log.i(CLIENT, e + "");
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                dataInputStream.close();
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
