package sustainability_app.server.air_visual_api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import org.apache.http.client.utils.URIBuilder;
import sustainability_app.server.comm.GetWebRequest;
import sustainability_app.server.comm.PostWebRequest;

public final class AirVisualRequest {
    private final static String BASE_URL = "";

    private URIBuilder urlBuilder;
    
    public AirVisualRequest(final String apiKey) throws URISyntaxException {
        urlBuilder = new URIBuilder(BASE_URL);
        urlBuilder.addParameter("apiKey", apiKey);
    }
    
    public AirVisualRequest addParameter(final String apiKey, final String value) {
        urlBuilder.addParameter(apiKey, value);
        return this;
    }
    
    public GetWebRequest get() throws MalformedURLException, IOException,
    URISyntaxException {
        final GetWebRequest getWebRequest = new GetWebRequest(urlBuilder, "application/json");
        return getWebRequest;
    }
    
    public PostWebRequest post(final String input) throws MalformedURLException, IOException,
    URISyntaxException {
        final PostWebRequest postWebRequest = new PostWebRequest(urlBuilder, "application/json");
        return postWebRequest.write(input);
    }
}
