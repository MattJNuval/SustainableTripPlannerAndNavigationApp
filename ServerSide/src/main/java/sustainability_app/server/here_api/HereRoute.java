package sustainability_app.server.here_api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.here.flexpolyline.PolylineEncoderDecoder;
import com.here.flexpolyline.PolylineEncoderDecoder.LatLngZ;

public final class HERERoute {
    private final JSONObject answer;
    
    public HERERoute(final String API_KEY, final LatLngZ origin,
            final LatLngZ destination, final String transportMode,
            final String alternatives,  final String returnType)
        throws JSONException, IOException, URISyntaxException {
        final HERERequest hereRequest = new HERERequest(API_KEY);
        hereRequest.addParameter("origin", origin.lat + "," + origin.lng)
        .addParameter("destination", destination.lat + "," + destination.lng)
        .addParameter("transportMode", transportMode)
        .addParameter("alternatives", alternatives)
        .addParameter("return", returnType);
        final JSONTokener tokener = new JSONTokener(hereRequest.get().readResponse());

        answer = new JSONObject(tokener);
    }
    
    public List<LatLngZ> polyline(int route, int section) throws JSONException {
        final String encodedCoordinates = section(route, section).getString("polyline");
        return PolylineEncoderDecoder.decode(encodedCoordinates);
    }
    
    public JSONObject route(int route) throws JSONException {
        return routeArray().getJSONObject(route);
    }
    
    public JSONArray routeArray() throws JSONException {
        return answer.getJSONArray("routes");
    }
    
    public JSONObject section(int route, int section) throws JSONException {
        return sectionArray(route).getJSONObject(section);
    }
    
    public JSONArray sectionArray(int route) throws JSONException {
        return this.route(route).getJSONArray("sections");
    }
}
