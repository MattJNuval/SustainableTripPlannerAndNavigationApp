package sustainability_app.server.here_api;

import java.net.URISyntaxException;
import sustainability_app.server.comm.APIWebRequest;

public final class HERERequest extends APIWebRequest {
    private final static String BASE_URL = "https://router.hereapi.com/v8/routes";
    
    public HERERequest(final String apiKey) throws URISyntaxException {
        super(BASE_URL, apiKey);
        urlBuilder.addParameter("apiKey", apiKey);
    }
}
