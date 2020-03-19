import java.util.Map;

public class Driver {
    private ServerCommunication serverCommunication;
    
    public Driver() { }
    
    public static void main(final String[] args)
            throws IllegalArgumentException {
        
        if (args.length >= 1) {
            final int portNumber = Integer.parseInt(args[0]);
            System.out.println("CloudJam 2020 Sustainable Trip Planner and Navigation "
                    + "App Server starting on port " + portNumber + ".");
            
            final Driver driver = new Driver();
            driver.serverCommunication = new ServerCommunication(portNumber).listen();
            
            String line = "";
            while (!line.equals("Over")) {
                final Map<String, String> data = driver.serverCommunication.data();
                for (Map.Entry<String, String> entry :
                    data.entrySet()) {
                    if (entry.getKey() != null && entry.getValue() != null) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                        line = entry.getValue();
                    }
                }
                data.clear();
            }
            driver.serverCommunication.close();
        } else {
            throw new IllegalArgumentException("Wrong number of arguments.");
        }
        
    }
}