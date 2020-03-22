package sustainability_app.server.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractWebRequest {
    private final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private final URL url;
    protected final HttpURLConnection conn;
    
    protected AbstractWebRequest(String urlString, String method)
            throws MalformedURLException, IOException {
        this.url = new URL(urlString);
        conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod(method);
        
        LOGGER.log(Level.INFO, "Server opening connection to " + url.toString());
    }
    
    public abstract String read() throws IOException;
    
    protected String output()
            throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String output = "";

        while ((output += br.readLine()) != null) { }
        
        LOGGER.log(Level.INFO, "Server recieved output from " + url.toString() + ": " + output);
        
        br.close();
        conn.disconnect();
        return output;
    }
}
