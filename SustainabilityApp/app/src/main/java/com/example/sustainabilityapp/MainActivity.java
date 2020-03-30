package com.example.sustainabilityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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

    private final static String CLIENT = "CLIENT";

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
        maps.toSearch(searchText);
        //Thread client = new Client("3.86.111.23", 5056, "");
        //client.start();
    }

    public void currentButton(View view) {
        maps.toCurrentPosition();
    }

    public void resetButton(View view) {
        maps.toReset();
    }

    public void pingButton(View view) {
        /*Thread client = new Client("3.86.111.23", 5056, "{\n" +
                "  \"clientCommand\": \"route-get\",\n" +
                "  \"originLat\": \"34.0687464\",\n" +
                "  \"originLon\": \"-118.3111569\",\n" +
                "  \"destinationLat\": \"34.0686074\",\n" +
                "  \"destinationLon\": \"-118.2924265\"\n" +
                "}");
        client.start(); */
        JsonString jsonString = new JsonString();
        // JSONStringParser jsonStringParser = new JSONStringParser(jsonString.RouteJson());
        // jsonStringParser.toPing();
        maps.toPing(jsonString.RouteJson());
        // Log.i(CLIENT, jsonString.RouteJson() + "");

    }

}
