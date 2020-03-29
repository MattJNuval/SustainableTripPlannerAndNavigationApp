package com.example.sustainabilityapp;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.mapping.PositionIndicator;
import com.here.android.mpa.routing.CoreRouter;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.android.mpa.routing.Router;
import com.here.android.mpa.routing.RoutingError;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

public class HereMaps {


    private Activity mainActivity = null;
    private FragmentActivity mainFragmentActivity = null;

    private PositioningManager positioningManager;
    private PositionIndicator positionIndicator;
    private boolean paused = false;
    private boolean onCreateTrigger = true;
    private Map map = null;
    private MapRoute mapRoute = null;

    public HereMaps(Activity activity, FragmentActivity fragmentActivity) {
        mainActivity = activity;
        mainFragmentActivity = fragmentActivity;
    }


    public void toCurrentPosition() {
        map.setCenter(positioningManager.getPosition().getCoordinate(),
                Map.Animation.BOW);
    }

    public void toReset() {
        if(map != null && mapRoute != null) {
            Toast.makeText(mainActivity, "Resetting", Toast.LENGTH_SHORT).show();
            map.removeMapObject(mapRoute);
            mapRoute = null;
        } else {
            Toast.makeText(mainActivity, "Resetting", Toast.LENGTH_SHORT).show();
        }
    }

    private void createRoute() {
        CoreRouter coreRouter = new CoreRouter();

        RoutePlan routePlan = new RoutePlan();

        RouteOptions routeOptions = new RouteOptions();
        routeOptions.setTransportMode(RouteOptions.TransportMode.CAR);
        routeOptions.setHighwaysAllowed(true);
        routeOptions.setRouteType(RouteOptions.Type.BALANCED);
        routeOptions.setRouteCount(1);
        routePlan.setRouteOptions(routeOptions);


        RouteWaypoint startPoint = new RouteWaypoint(positioningManager.getPosition().getCoordinate());
        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(34.0537633,-118.2419223));
        // RouteWaypoint destination2 = new RouteWaypoint(new GeoCoordinate(34.2410366,-118.5276745));

        routePlan.addWaypoint(startPoint);
        routePlan.addWaypoint(destination);
        // routePlan.addWaypoint(destination2);

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

    public void toSearch() {
        if(map != null && mapRoute != null) {
            map.removeMapObject(mapRoute);
            mapRoute = null;
        } else {
            createRoute();
        }
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
