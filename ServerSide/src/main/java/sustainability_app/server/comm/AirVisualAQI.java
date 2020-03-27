package sustainability_app.server.comm;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.here.flexpolyline.PolylineEncoderDecoder.LatLngZ;

/**
 * API for AirVisual AQI.
 * @see <a href="https://documenter.getpostman.com/view/507654/airvisual-api/">API Implementation</a>
 */
public final class AirVisualAQI {
    private final JSONObject answer;

    private final class AirVisualAQIRequest extends APIWebRequest {
        private final static String BASE_URL = "https://api.airvisual.com/v2/nearest_city";
        
        /**
         * Constructor for an AirVisual API web request.
         * @param apiKey {@link String} for the access API key.
         * @throws URISyntaxException if URL has a syntax error.
         */
        public AirVisualAQIRequest(final String apiKey) throws URISyntaxException {
            super(BASE_URL);
            uriBuilder.addParameter("key", apiKey);
        }
    }
    
    /**
     * Constructor for a server and client communication.
     * @param apiKey {@link String} for the access API key.
     * @param coords {@link LatLngZ} for the coordinates.
     * @throws JSONException
     * @throws MalformedURLException
     * @throws IOException
     * @throws URISyntaxException
     */
    public AirVisualAQI(final String apiKey, final LatLngZ coords)
            throws JSONException, MalformedURLException,
            IOException, URISyntaxException {
        final AirVisualAQIRequest airVisualRequest = new AirVisualAQIRequest(apiKey);
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
