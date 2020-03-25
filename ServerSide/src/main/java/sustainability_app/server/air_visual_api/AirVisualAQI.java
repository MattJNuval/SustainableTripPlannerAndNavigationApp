package sustainability_app.server.air_visual_api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.here.flexpolyline.PolylineEncoderDecoder.LatLngZ;

public class AirVisualAQI {
    private final JSONObject answer;
    
    public AirVisualAQI(final String API_KEY, final LatLngZ coords)
            throws JSONException, MalformedURLException,
            IOException, URISyntaxException {
        final AirVisualRequest airVisualRequest = new AirVisualRequest(API_KEY);
        airVisualRequest.addParameter("lat", "" + coords.lat)
        .addParameter("lon", "" + coords.lng);
        final JSONTokener tokener = new JSONTokener(airVisualRequest.get().readResponse());

        answer = new JSONObject(tokener);
    }
    
    public Number AQIUS() throws JSONException {
        return pollution().getNumber("aqius");
    }
    
    public JSONObject current() throws JSONException {
        return data().getJSONObject("current");
    }
    
    public JSONObject data() throws JSONException {
        return answer.getJSONObject("data");
    }
    
    public JSONObject pollution() {
        return current().getJSONObject("pollution");
    }
}
