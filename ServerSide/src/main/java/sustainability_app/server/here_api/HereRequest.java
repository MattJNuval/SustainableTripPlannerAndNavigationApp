package sustainability_app.server.here_api;

import java.net.URISyntaxException;
import sustainability_app.server.comm.APIWebRequest;

public final class HereRequest extends APIWebRequest {
    private final static String BASE_URL = "https://router.hereapi.com/v8/routes";
    
    public HereRequest(final String apiKey) throws URISyntaxException {
        super(BASE_URL, apiKey);
    }
}
