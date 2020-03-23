package sustainability_app.server.air_visual_api;

import java.net.URISyntaxException;
import sustainability_app.server.comm.APIWebRequest;

public final class AirVisualRequest extends APIWebRequest {
    private final static String BASE_URL = "";
    
    public AirVisualRequest(final String apiKey) throws URISyntaxException {
        super(BASE_URL, apiKey);
    }
}
