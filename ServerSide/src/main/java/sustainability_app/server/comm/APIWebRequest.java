package sustainability_app.server.comm;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;

/**
 * Abstraction for an API web request container.
 */
public abstract class APIWebRequest {
    /**
     * {@link URIBuilder} for building url.
     */
    protected URIBuilder urlBuilder;
    
    /**
     * Constructor for an API web request.
     * @param baseUrl {@link String} for the API base url.
     * @throws URISyntaxException.
     */
    protected APIWebRequest(final String baseUrl) throws URISyntaxException {
        urlBuilder = new URIBuilder(baseUrl);
    }
    
    /**
     * Add parameter to API web request.
     * @param key {@link String} for the parameter key.
     * @param value {@link String} for the parameter value.
     * @return {@link APIWebRequest} of this.
     */
    public APIWebRequest addParameter(final String key, final String value) {
        urlBuilder.addParameter(key, value);
        return this;
    }
    
    /**
     * Performs get request on API web request.
     * @return {@link GetWebRequest} for web request.
     * @throws MalformedURLException.
     * @throws IOException.
     * @throws URISyntaxException.
     */
    public GetWebRequest get() throws MalformedURLException, IOException,
    URISyntaxException {
        return new GetWebRequest(urlBuilder, "application/json");
    }
    
    /**
     * Performs post request on API web request.
     * @param input {@link String} for the input of the post request.
     * @return {@link GetWebRequest} for web request.
     * @throws MalformedURLException if url is malformed.
     * @throws IOException if an IO error occurred.
     * @throws URISyntaxException if url has a syntax error.
     */
    public PostWebRequest post(final String input) throws MalformedURLException, IOException,
    URISyntaxException {
        return new PostWebRequest(urlBuilder, "application/json").write(input);
    }
}
