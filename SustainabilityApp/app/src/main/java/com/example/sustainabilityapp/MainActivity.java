package com.example.sustainabilityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

    private PermissionChecker permissionChecker = null;
    private HereMaps maps = null;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionChecker = new PermissionChecker(this);
        permissionChecker.checkPermissions();

        maps = new HereMaps(this, this);
        maps.initialize();
    }

    public void searchButton(View view) {
        searchEditText = (EditText)findViewById(R.id.search);
        String searchText = searchEditText.getText().toString();
        Thread searchThread = new Client(searchText);
        searchThread.start();
    }

    public void currentButton(View view) {
        maps.toCurrentPosition();
    }

    public void resetButton(View view) {
        maps.toReset();
    }

    public void pingButton(View view) {
        Thread pingThread = new Client("Ping");
        pingThread.start();
    }



}
