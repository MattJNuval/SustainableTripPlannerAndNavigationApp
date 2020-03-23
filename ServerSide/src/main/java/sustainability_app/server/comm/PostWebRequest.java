package sustainability_app.server.comm;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.http.client.utils.URIBuilder;

public final class PostWebRequest
    extends AbstractWebRequest {
    
    public PostWebRequest(final URL url, final String contentType)
            throws MalformedURLException, IOException {
        super(url, "POST");
        conn.setRequestProperty("Content-Type", contentType);
    }

    public PostWebRequest(final URIBuilder urlBuilder, final String contentType)
            throws MalformedURLException, IOException, URISyntaxException {
        super(urlBuilder, "POST");
        conn.setRequestProperty("Content-Type", contentType);
    }
    
    public PostWebRequest write(String input)
            throws IOException {
        final OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();
        return this;
    }
    
    @Override
    public String read() throws IOException {
        if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
            throw new IOException("Failed, HTTP error code: "
                    + conn.getResponseCode());
        }
        return super.output();
    }
}
