package sustainability_app.server.comm;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * POST web request.
 */
public final class PostWebRequest
    extends AbstractWebRequest {
    
    /**
     * Constructor for a POST web request using a {@link URL}.
     * @param url {@link URL} for the web request.
     * @param contentType {@link String} for the web request content type method.
     * @throws IOException if an IO error occurred.
     */
    public PostWebRequest(final URL url, final String contentType) throws IOException {
        super(url, "POST");
        conn.setRequestProperty("Content-Type", contentType);
    }
    
    /**
     * Write to POST web request.
     * @param input {@link String} for the POST web request input.
     * @return {@link PostWebRequest} of this.
     * @throws IOException if an IO error occurred.
     */
    public PostWebRequest write(String input)
            throws IOException {
        final OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();
        return this;
    }
    
    @Override
    public String readResponse() throws IOException {
        if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
            throw new IOException("Failed, HTTP error code: "
                    + conn.getResponseCode());
        }
        return super.output();
    }
}
