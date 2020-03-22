package sustainability_app.server.comm;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public final class PostWebRequest
    extends AbstractWebRequest {
    public PostWebRequest(String urlString, String contentType)
            throws MalformedURLException, IOException {
        super(urlString, "POST");
        conn.setRequestProperty("Content-Type", contentType);
    }
    
    public PostWebRequest(StringBuilder urlString, String contentType)
            throws MalformedURLException, IOException {
        this(urlString.toString(), contentType);
    }
    
    public PostWebRequest write(String input)
            throws IOException {
        final OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();
        return this;
    }
    
    @Override
    public String read()
            throws IOException {
        if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
            throw new IOException("Failed, HTTP error code: "
                    + conn.getResponseCode());
        }
        return super.output();
    }
}
