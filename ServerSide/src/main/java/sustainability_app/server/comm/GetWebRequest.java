package sustainability_app.server.comm;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * GET web request.
 */
public final class GetWebRequest
    extends AbstractWebRequest {
    
    /**
     * Constructor for a GET web request using a {@link URL}.
     * @param url {@link URL} for the web request.
     * @param accept {@link String} for the web request accepting method.
     * @throws IOException if an IO error occurred.
     */
    public GetWebRequest(final URL url, final String accept) throws IOException {
        super(url, "GET");
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
