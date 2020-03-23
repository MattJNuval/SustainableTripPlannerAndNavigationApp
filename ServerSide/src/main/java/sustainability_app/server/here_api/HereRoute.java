package sustainability_app.server.here_api;

import com.here.flexpolyline.PolylineEncoderDecoder;
import com.here.flexpolyline.PolylineEncoderDecoder.LatLngZ;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public final class HERERoute {
    private final JSONObject answer;
    
    public HERERoute(final String API_KEY, final String origin,
            final String destination, final String transportMode,
            final String alternatives,  final String returnType)
        throws JSONException, MalformedURLException, IOException, URISyntaxException {
        /*final HERERequest hereRequest = new HERERequest(API_KEY);
        hereRequest.addParameter("origin", origin);
        hereRequest.addParameter("destination", destination);
        hereRequest.addParameter("transportMode", transportMode);
        hereRequest.addParameter("alternatives", alternatives);
        hereRequest.addParameter("return", returnType);
        final JSONTokener tokener = new JSONTokener(hereRequest.get().read());*/
        
        final JSONTokener tokener = new JSONTokener("{\"routes\":[{\"id\":\"42af46d9-785b-458d-b39f-2d4ff03293aa\",\"sections\":[{\"id\":\"3198e40a-89e4-408e-98db-c50f68d271ba\",\"type\":\"vehicle\",\"departure\":{\"place\":{\"type\":\"place\",\"location\":{\"lat\":52.5309837,\"lng\":13.384567,\"elv\":0.0},\"originalLocation\":{\"lat\":52.5307999,\"lng\":13.3847}}},\"arrival\":{\"place\":{\"type\":\"place\",\"location\":{\"lat\":52.5323264,\"lng\":13.378874,\"elv\":0.0},\"originalLocation\":{\"lat\":52.5323,\"lng\":13.3789}}},\"polyline\":\"BGwynmkDu39wZvBtF3InfvHrdvHvboGzF0FnGoGvHsOvR8L3NkSnVoGjIsEzFgFvHkDrJwHrJwHrJ4NjS0ezoBjInV3N_iBzJ_Z\",\"transport\":{\"mode\":\"truck\"}}]}]}");
        
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
