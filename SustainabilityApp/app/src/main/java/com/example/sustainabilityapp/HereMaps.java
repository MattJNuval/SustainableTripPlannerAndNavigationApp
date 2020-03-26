package com.example.sustainabilityapp;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;

import java.io.File;

public class HereMaps {

    private Activity mainActivity = null;
    private FragmentActivity mainFragmentActivity = null;

    public HereMaps(Activity activity, FragmentActivity fragmentActivity) {
        mainActivity = activity;
        mainFragmentActivity = fragmentActivity;
    }


    public void initialize() {
        // Search for the Map Fragment
        final AndroidXMapFragment mapFragment = (AndroidXMapFragment)
                mainFragmentActivity.getSupportFragmentManager().findFragmentById(R.id.mapfragment);

        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(
                mainActivity.getApplicationContext().getExternalFilesDir(null) + File.separator + ".here-maps",
                "com.example.sustainabilityapp.MapService");

        if(!success) {
            // Setting the isolated disk cache was not successful, please check if the path is valid and
            // ensure that it does not match the default location
            // (getExternalStorageDirectory()/.here-maps).
            // Also, ensure the provided intent name does not match the default intent name.
        } else {
            // Initialize the Map Fragment and
            // retrieve the map that is associated to the fragment
            mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(Error error) {
                    if (error == Error.NONE) {
                        Map map = mapFragment.getMap();

                        // Set the map center to Vancouver, Canada.
                        map.setCenter(new GeoCoordinate(49.196261,
                                -123.004773), Map.Animation.NONE);

                    } else {
                        System.out.println("ERROR: Cannot initialize AndroidXMapFragment");
                    }
                }
            });
        }
    }

}
