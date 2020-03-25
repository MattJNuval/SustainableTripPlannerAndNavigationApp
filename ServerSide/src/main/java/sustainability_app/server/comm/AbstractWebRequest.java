package sustainability_app.server.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstraction for a web request including reading and outputting to web routes.
 */
public abstract class AbstractWebRequest {
    private final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private final URL url;
    
    /**
     * {@link HttpURLConnection} for web request.
     */
    protected final HttpURLConnection conn;

    /**
     * Constructor for a web request using a {@link URL}.
     * @param url {@link URL} for the web request.
     * @param method {@link String} for the web request.
     * @throws IOException if an IO error occurred.
     */
    protected AbstractWebRequest(final URL url, final String method) throws IOException {
        this.url = url;
        // Establishes a connection.
        conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        
        LOGGER.log(Level.INFO, "Server opening connection to " + url.toString());
    }
    
    /**
     * Reading response from web request.
     * @return {@link String} for the results.
     * @throws IOException if an IO error occurred.
     */
    public abstract String readResponse() throws IOException;
    
    /**
     * Reading output from web request.
     * @return {@link String} for the output.
     * @throws IOException if an IO error occurred.
     */
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
