package sustainability_app.server.comm;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.client.utils.URIBuilder;

public final class GetWebRequest
    extends AbstractWebRequest {
    
    public GetWebRequest(final URL url, final String accept)
            throws MalformedURLException, IOException {
        super(url, "GET");
        conn.setRequestProperty("Accept", accept);
    }

    public GetWebRequest(final URIBuilder urlBuilder, final String accept)
            throws MalformedURLException, IOException, URISyntaxException {
        super(urlBuilder, "GET");
        conn.setRequestProperty("Accept", accept);
    }
    
    @Override
    public String read() throws IOException {
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed, HTTP error code: "
                    + conn.getResponseCode());
        }
        return super.output();
    }
}
