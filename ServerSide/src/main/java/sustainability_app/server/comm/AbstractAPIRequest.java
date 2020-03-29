package sustainability_app.server.comm;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;

/**
 * Abstraction for an API request container.
 */
public abstract class AbstractAPIRequest {
    /**
     * {@link URIBuilder} for building URI.
     */
    protected URIBuilder uriBuilder;
    
    /**
     * Constructor for an API request.
     * @param baseURI {@link String} for the API base URI.
     * @throws URISyntaxException if URL has a syntax error.
     */
    protected AbstractAPIRequest(final String baseURI) throws URISyntaxException {
        uriBuilder = new URIBuilder(baseURI);
    }
    
    /**
     * Add parameter to API request.
     * @param key {@link String} for the parameter key.
     * @param value {@link String} for the parameter value.
     * @return {@link APIWebRequest} of this.
     */
    public AbstractAPIRequest addParameter(final String key, final String value) {
        uriBuilder.addParameter(key, value);
        return this;
    }
    
    /**
     * Performs GET web request on API request.
     * @return {@link GetWebRequest} for web request.
     * @throws MalformedURLException if URL is malformed.
     * @throws IOException if an IO error occurred.
     * @throws URISyntaxException if URL has a syntax error.
     */
    public GetWebRequest get() throws MalformedURLException, IOException,
    URISyntaxException {
        return new GetWebRequest(uriBuilder.build().toURL(), "application/json");
    }
    
    /**
     * Performs POST web request on API request.
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
