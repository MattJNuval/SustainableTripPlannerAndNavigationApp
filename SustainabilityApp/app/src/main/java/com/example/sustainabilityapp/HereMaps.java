package com.example.sustainabilityapp;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.PositionIndicator;

import java.io.File;
import java.lang.ref.WeakReference;

public class HereMaps {


    private Activity mainActivity = null;
    private FragmentActivity mainFragmentActivity = null;

    private PositioningManager positioningManager;
    private PositionIndicator positionIndicator;
    private boolean paused = false;
    private boolean onCreateTrigger = true;
    private Map map = null;

    public HereMaps(Activity activity, FragmentActivity fragmentActivity) {
        mainActivity = activity;
        mainFragmentActivity = fragmentActivity;
    }

    private PositioningManager.OnPositionChangedListener positionChangedListener = new PositioningManager.OnPositionChangedListener() {
        @Override
        public void onPositionUpdated(PositioningManager.LocationMethod locationMethod, GeoPosition geoPosition, boolean b) {
            if(onCreateTrigger) {
                map.setCenter(positioningManager.getPosition().getCoordinate(),Map.Animation.BOW);
                onCreateTrigger = false;
            }
            if(positioningManager != null) {
                positioningManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
            }
        }

        @Override
        public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {

        }
    };


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
                        map = mapFragment.getMap();

                        map.setCenter(new GeoCoordinate(34.0589578,-118.3027765,0),
                                Map.Animation.NONE);
                        map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);

                        if(positioningManager == null) {
                            positioningManager = PositioningManager.getInstance();
                            positioningManager.addListener(new WeakReference<>(positionChangedListener));
                            positioningManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
                        }

                        positionIndicator = map.getPositionIndicator();
                        positionIndicator.setVisible(true);
                        positionIndicator.setAccuracyIndicatorVisible(true);



                    } else {
                        System.out.println("ERROR: Cannot initialize AndroidXMapFragment");
                    }
                }
            });
        }
    }

}
