package com.example.sustainabilityapp;

import android.util.Log;

import java.util.concurrent.BlockingQueue;

public class Consumer extends Thread {

    private final static String HERE = "HERE";

    private String data;
    protected BlockingQueue queue = null;

    public Consumer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            Log.i(HERE,queue.take()+"");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
