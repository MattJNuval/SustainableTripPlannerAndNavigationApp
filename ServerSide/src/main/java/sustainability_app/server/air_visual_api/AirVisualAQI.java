package sustainability_app.server.air_visual_api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import sustainability_app.server.Coordinates;

public class AirVisualAQI {
    private final JSONObject answer;
    
    public AirVisualAQI(final String API_KEY, final Coordinates coords)
            throws JSONException, MalformedURLException,
            IOException, URISyntaxException {
        final AirVisualRequest airVisualRequest = new AirVisualRequest(API_KEY);
        airVisualRequest.addParameter("lat", coords.lat);
        airVisualRequest.addParameter("lon", coords.lon);
        final JSONTokener tokener = new JSONTokener(airVisualRequest.get().read());

        answer = new JSONObject(tokener);
        System.out.println(answer);
    }
}
