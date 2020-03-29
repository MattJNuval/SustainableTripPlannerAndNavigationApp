package sustainability_app.server.comm;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.here.flexpolyline.PolylineEncoderDecoder;
import com.here.flexpolyline.PolylineEncoderDecoder.LatLngZ;

/**
 * API for HERE Route.
 * @see <a href="https://developer.here.com/documentation/routing-api/">API Implementation</a>
 */
public final class HERERoute {
    private final JSONObject answer;

    private final class HERERouteRequest extends AbstractAPIRequest {
        private final static String BASE_URL = "https://router.hereapi.com/v8/routes";

        /**
         * Constructor for an HERE Route API request.
         * @param apiKey {@link String} for the access API key.
         * @throws URISyntaxException if URL has a syntax error.
         */
        public HERERouteRequest(final String apiKey) throws URISyntaxException {
            super(BASE_URL);
            uriBuilder.addParameter("apiKey", apiKey);
        }
    }
    
    /**
     * Constructor for an HERE web request.
     * @param apiKey {@link String} for the access API key.
     * @param origin {@link LatLngZ} for the origin coordinates.
     * @param destination {@link LatLngZ} for the destination coordinates.
     * @param transportMode {@link String} for type of transport.
     * @param alternatives {@link String} for number of alternatives.
     * @param returnType {@link String} for type of route return.
     * @throws JSONException if a JSON error occurred.
     * @throws IOException if an IO error occurred.
     * @throws URISyntaxException if URL has a syntax error.
     */
    public HERERoute(final String apiKey, final LatLngZ origin,
            final LatLngZ destination, final String transportMode,
            final int alternatives,  final String returnType)
        throws JSONException, IOException, URISyntaxException {
        final HERERouteRequest hereRequest = new HERERouteRequest(apiKey);
        hereRequest.addParameter("origin", origin.lat + "," + origin.lng)
        .addParameter("destination", destination.lat + "," + destination.lng)
        .addParameter("transportMode", transportMode)
        .addParameter("alternatives", "" + alternatives)
        .addParameter("return", returnType);
        final JSONTokener tokener = new JSONTokener(hereRequest.get().readResponse());

        answer = new JSONObject(tokener);
    }
    
    /**
     * Retrieves polyline of route and section.
     * @param route {@link int} for the route number.
     * @param section {@link int} for the section number.
     * @return {@link List} of {@link LatLngZ} of polyline coordinates.
     * @throws JSONException if a JSON error occurred.
     */
    public List<LatLngZ> polyline(int route, int section) throws JSONException {
        final String encodedCoordinates = section(route, section).getString("polyline");
        return PolylineEncoderDecoder.decode(encodedCoordinates);
    }
    
    /**
     * Retrieves a route from all possible routes.
     * @param route {@link int} for the route number.
     * @return {@link JSONObject} of the route.
     * @throws JSONException if a JSON error occurred.
     */
    public JSONObject route(int route) throws JSONException {
        return routeArray().getJSONObject(route);
    }
    
    /**
     * Retrieves all possible routes of current request.
     * @return {@link JSONArray} of the routes.
     * @throws JSONException if a JSON error occurred.
     */
    public JSONArray routeArray() throws JSONException {
        return answer.getJSONArray("routes");
    }
    
    /**
     * Retrieves section from all possible sections.
     * @param route {@link int} for the route number.
     * @param section {@link int} for the section number.
     * @return {@link JSONObject} of the section.
     * @throws JSONException if a JSON error occurred.
     */
    public JSONObject section(int route, int section) throws JSONException {
        return sectionArray(route).getJSONObject(section);
    }
    
    /**
     * Retrieves all possible sections from a route.
     * @param route {@link int} for the route number.
     * @return {@link JSONArray} of the sections.
     * @throws JSONException if a JSON error occurred.
     */
    public JSONArray sectionArray(int route) throws JSONException {
        return this.route(route).getJSONArray("sections");
    }
}
