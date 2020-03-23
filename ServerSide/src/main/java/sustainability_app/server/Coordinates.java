package sustainability_app.server;

public class Coordinates {
    public String lat;
    public String lon;
    public String z;
    
    public Coordinates(final String lat, final String lon) {
        this(lat, lon, "0.0");
    }
    
    public Coordinates(final String lat, final String lon, final String z) {
        this.lat = lat;
        this.lon = lon;
        this.z = z;
    }
    
    public String toString2D() {
        return lat + "," + lon;
    }
    
    public String toStringFull() {
        return toString2D() + "," + z;
    }
}
