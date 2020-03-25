package sustainability_app.server.comm;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.client.utils.URIBuilder;

/**
 * GET web request.
 */
public final class GetWebRequest
    extends AbstractWebRequest {
    
    /**
     * Constructor for a GET web request using a {@link URL}.
     * @param url {@link URL} for the web request.
     * @param accept {@link String} for the web request accepting method.
     * @throws MalformedURLException if url is malformed.
     * @throws IOException if an IO error occurred.
     */
    public GetWebRequest(final URL url, final String accept)
            throws MalformedURLException, IOException {
        super(url, "GET");
        conn.setRequestProperty("Accept", accept);
    }

    /**
     * Constructor for a GET web request using a {@link URIBuilder}.
     * @param urlBuilder {@link URIBuilder} for the web request.
     * @param accept {@link String} for the web request accepting method.
     * @throws MalformedURLException if url is malformed.
     * @throws IOException if an IO error occurred.
     * @throws URISyntaxException if url has a syntax error.
     */
    public GetWebRequest(final URIBuilder urlBuilder, final String accept)
            throws MalformedURLException, IOException, URISyntaxException {
        super(urlBuilder, "GET");
        conn.setRequestProperty("Accept", accept);
    }
    
    @Override
    public String readResponse() throws IOException {
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed, HTTP error code: "
                    + conn.getResponseCode());
        }
        return super.output();
    }
}
