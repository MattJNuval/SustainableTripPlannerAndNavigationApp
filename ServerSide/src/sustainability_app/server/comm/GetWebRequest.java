package sustainability_app.server.comm;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public final class GetWebRequest
    extends AbstractWebRequest {
    public GetWebRequest(String urlString, String accept)
            throws MalformedURLException, IOException {
        super(urlString, "POST");
        conn.setRequestProperty("Accept", accept);
    }
    
    public GetWebRequest(StringBuilder urlString, String contentType)
            throws MalformedURLException, IOException {
        this(urlString.toString(), contentType);
    }
    
    @Override
    public String read()
            throws IOException {
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed, HTTP error code: "
                    + conn.getResponseCode());
        }
        return super.output();
    }
}
