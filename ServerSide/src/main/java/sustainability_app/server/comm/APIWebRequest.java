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
     * {@link URIBuilder} for building URI.
     */
    protected URIBuilder uriBuilder;
    
    /**
     * Constructor for an API web request.
     * @param baseURI {@link String} for the API base URI.
     * @throws URISyntaxException.
     */
    protected APIWebRequest(final String baseURI) throws URISyntaxException {
        uriBuilder = new URIBuilder(baseURI);
    }
    
    /**
     * Add parameter to API web request.
     * @param key {@link String} for the parameter key.
     * @param value {@link String} for the parameter value.
     * @return {@link APIWebRequest} of this.
     */
    public APIWebRequest addParameter(final String key, final String value) {
        uriBuilder.addParameter(key, value);
        return this;
    }
    
    /**
     * Performs GET request on API web request.
     * @return {@link GetWebRequest} for web request.
     * @throws MalformedURLException.
     * @throws IOException.
     * @throws URISyntaxException.
     */
    public GetWebRequest get() throws MalformedURLException, IOException,
    URISyntaxException {
        return new GetWebRequest(uriBuilder.build().toURL(), "application/json");
    }
    
    /**
     * Performs POST request on API web request.
     * @param input {@link String} for the input of the post request.
     * @return {@link GetWebRequest} for web request.
     * @throws MalformedURLException if URL is malformed.
     * @throws IOException if an IO error occurred.
     * @throws URISyntaxException if URL has a syntax error.
     */
    public PostWebRequest post(final String input) throws MalformedURLException, IOException,
    URISyntaxException {
        return new PostWebRequest(uriBuilder.build().toURL(), "application/json").write(input);
    }
}
