package sustainability_app.server.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.utils.URIBuilder;

public abstract class AbstractWebRequest {
    private final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private final URL url;
    protected final HttpURLConnection conn;

    protected AbstractWebRequest(final URL url, final String method)
            throws MalformedURLException, IOException {
        this.url = url;
        conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        
        LOGGER.log(Level.INFO, "Server opening connection to " + url.toString());
    }
    
    protected AbstractWebRequest(final URIBuilder urlBuilder, final String method)
            throws MalformedURLException, IOException, URISyntaxException {
        this(urlBuilder.build().toURL(), method);
    }
    
    public abstract String read() throws IOException;
    
    protected String output() throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String output = "";
        String clearOutput = "";

        while ((output = br.readLine()) != null) {
            clearOutput += output;
        }
        
        LOGGER.log(Level.INFO, "Server recieved output from " + url.toString() + ":\n" + clearOutput);
        
        br.close();
        conn.disconnect();
        return clearOutput;
    }
}
