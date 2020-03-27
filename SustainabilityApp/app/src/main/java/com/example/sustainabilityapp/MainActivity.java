package com.example.sustainabilityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.here.android.mpa.cluster.ClusterLayer;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String CLIENT = "CLIENT";


    private PermissionChecker permissionChecker = null;
    private HereMaps maps = null;
    private Client client = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionChecker = new PermissionChecker(this);
        permissionChecker.checkPermissions();

        maps = new HereMaps(this, this);
        maps.initialize();
    }

    public void currentButton(View view) {
        maps.toCurrentPosition();
    }

    public void resetButton(View view) {
        maps.toReset();
    }

    public void startClientButton(View view) {
    }

    public void exitButton(View view) {
        Thread exitThread = new Client("Exit");
        exitThread.start();
    }

    public void getDateButton(View view) {
        Thread exitThread = new Client("Date");
        exitThread.start();
    }

    public void getTimeButton(View view) throws IOException {
        Thread exitThread = new Client("Time");
        exitThread.start();
    }

}
