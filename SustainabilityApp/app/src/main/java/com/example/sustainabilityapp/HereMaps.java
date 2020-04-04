package com.example.sustainabilityapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapLabeledMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.mapping.PositionIndicator;
import com.here.android.mpa.routing.CoreRouter;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.android.mpa.routing.Router;
import com.here.android.mpa.routing.RoutingError;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HereMaps {

    private final static String CLIENT = "CLIENT";

    private Activity mainActivity = null;
    private FragmentActivity mainFragmentActivity = null;

    private PositioningManager positioningManager;
    private PositionIndicator positionIndicator;
    private boolean onCreateTrigger = true;
    private Map map = null;
    private MapRoute mapRoute = null;
    private Image image;
    private MapLabeledMarker mapLabeledMarker;
    private List<MapObject> mapObjectList;

    /**
     * Constructor
     * @param activity
     * @param fragmentActivity
     */
    public HereMaps(Activity activity, FragmentActivity fragmentActivity) {
        mainActivity = activity;
        mainFragmentActivity = fragmentActivity;
    }

    /**
     * Set the marker with given aqi labels on the map using the latitude and longitude
     * @param latitude
     * @param longitude
     * @param aqi
     */
    public void setMarker(double latitude, double longitude, String aqi) {
        try {

            image = new Image();
            Bitmap icon = BitmapFactory.decodeResource(mainActivity.getApplicationContext().getResources(), R.drawable.ic_dot);
            image.setBitmap(icon);
            mapLabeledMarker = new MapLabeledMarker(new GeoCoordinate(latitude, longitude), image);
            mapLabeledMarker.setLabelText(map.getMapDisplayLanguage(),"AQI: " + aqi);
            mapObjectList.add(mapLabeledMarker);
            map.addMapObject(mapLabeledMarker);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To test server response
     * @param jsonString
     */
    public void toPing(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject jsonObject1 = jsonObject.getJSONObject("route0");
            Iterator<String> keys = jsonObject1.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                if(jsonObject1.get(key) instanceof  JSONObject) {
                    //if(!jsonObject1.getJSONObject(key).get("aqi").equals("?")) {
                        Log.i(CLIENT, "lat: " + jsonObject1.getJSONObject(key).get("lat") + "\n"
                                + "lon: " + jsonObject1.getJSONObject(key).get("lon") + "\n"
                                + "aqi: " + jsonObject1.getJSONObject(key).get("aqi") + "\n\n");
                        double latitude = Double.parseDouble(jsonObject1.getJSONObject(key).get("lat")+"");
                        double longitude = Double.parseDouble(jsonObject1.getJSONObject(key).get("lon")+"");
                        String aqi = jsonObject1.getJSONObject(key).get("aqi")+"";
                        setMarker(latitude,longitude,aqi);
                    //}
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Repositions map to user's current position
     */
    public void toCurrentPosition() {
        map.setCenter(positioningManager.getPosition().getCoordinate(),
                Map.Animation.BOW);
    }

    /**
     * Remove map marker and map routes from the map
     */
    public void toReset() {
        if(map != null && mapRoute != null) {
            Toast.makeText(mainActivity, "Resetting", Toast.LENGTH_SHORT).show();
            map.removeMapObject(mapRoute);
            map.removeMapObjects(mapObjectList);
            mapRoute = null;
        } else if(map != null && mapObjectList != null) {
            Toast.makeText(mainActivity, "Resetting", Toast.LENGTH_SHORT).show();
            map.removeMapObjects(mapObjectList);
        } else {
            Toast.makeText(mainActivity, "Resetting", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Parses jsonString to get latitude and longitude response from the server to create point to point coordinates leading up to final latitude and longitude coordinates
     * @param jsonString
     * @param finalLatitude
     * @param finalLongitude
     */
    private void createRoute(String jsonString, double finalLatitude, double finalLongitude) {

        CoreRouter coreRouter = new CoreRouter();

        RoutePlan routePlan = new RoutePlan();

        RouteOptions routeOptions = new RouteOptions();
        routeOptions.setTransportMode(RouteOptions.TransportMode.CAR);
        routeOptions.setHighwaysAllowed(true);
        routeOptions.setRouteType(RouteOptions.Type.BALANCED);
        routeOptions.setRouteCount(1);
        routePlan.setRouteOptions(routeOptions);

        RouteWaypoint startPoint = new RouteWaypoint(positioningManager.getPosition().getCoordinate());

        List<RouteWaypoint> routeWaypointList = new ArrayList<>();
        int index = 0;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject jsonObject1 = jsonObject.getJSONObject("route0");
            Iterator<String> keys = jsonObject1.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                if(jsonObject1.get(key) instanceof  JSONObject) {
                    //if(!jsonObject1.getJSONObject(key).get("aqi").equals("?")) {

                        double latitude = Double.parseDouble(jsonObject1.getJSONObject(key).get("lat")+"");
                        double longitude = Double.parseDouble(jsonObject1.getJSONObject(key).get("lon")+"");
                        String aqi = jsonObject1.getJSONObject(key).get("aqi")+"";
                        setMarker(latitude,longitude,aqi);
                        routeWaypointList.add(index,new RouteWaypoint(new GeoCoordinate(latitude, longitude)));
                        index++;

                    //}
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RouteWaypoint finalDestination = new RouteWaypoint(new GeoCoordinate(finalLatitude, finalLongitude));

        routePlan.addWaypoint(startPoint);
        for(int index2 = 0; index2 < routeWaypointList.size(); index2++) {
            routePlan.addWaypoint(routeWaypointList.get(index2));
        }
        routePlan.addWaypoint(finalDestination);

        coreRouter.calculateRoute(routePlan, new Router.Listener<List<RouteResult>, RoutingError>() {
            @Override
            public void onProgress(int i) {

            }

            @Override
            public void onCalculateRouteFinished(List<RouteResult> routeResults, RoutingError routingError) {
                if(routingError == RoutingError.NONE) {
                    if(routeResults.get(0).getRoute() != null) {
                        mapRoute = new MapRoute(routeResults.get(0).getRoute());
                        mapRoute.setManeuverNumberVisible(true);
                        map.addMapObject(mapRoute);
                        GeoBoundingBox geoBoundingBox = routeResults.get(0).getRoute().getBoundingBox();
                        map.zoomTo(geoBoundingBox, Map.Animation.BOW, Map.MOVE_PRESERVE_ORIENTATION);
                    } else {
                        Toast.makeText(mainActivity, "Error: route results returned is not valid", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mainActivity, "Error: route calculation returned error code: " + routingError, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void toSearch(String input, String jsonString, double finalLatitude, double finalLongitude) {
        if(map != null && mapRoute != null) {
            map.removeMapObject(mapRoute);
            map.removeMapObjects(mapObjectList);
            mapRoute = null;
        } else {
            if(input.equalsIgnoreCase("ralphs")) {
                createRoute(jsonString, finalLatitude, finalLongitude);
            }
        }
    }

    /**
     * To get user's current position
     */
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

    /**
     * Creates an initization from the map
     */
    public void initialize() {

        mapObjectList = new ArrayList<>();
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
